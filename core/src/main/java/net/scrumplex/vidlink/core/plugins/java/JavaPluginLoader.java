package net.scrumplex.vidlink.core.plugins.java;

import net.scrumplex.vidlink.core.plugins.*;
import net.scrumplex.vidlink.core.plugins.java.platforms.PlatformPluginLoader;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JavaPluginLoader implements PluginLoader {

	private Map<PluginType, PluginLoader> pluginTypeLoaders;

	public JavaPluginLoader() {
		this(new HashMap<>());
	}

	public JavaPluginLoader(@NotNull Map<PluginType, PluginLoader> pluginTypeLoaders) {
		this.pluginTypeLoaders = pluginTypeLoaders;

		if (this.pluginTypeLoaders.isEmpty()) { // Add default values if none specified.
			putPluginTypeLoader(PluginType.PLATFORM, new PlatformPluginLoader(this));
			// More will follow
		}
	}

	public void putPluginTypeLoader(@NotNull PluginType type, @NotNull PluginLoader loader) {
		pluginTypeLoaders.put(type, loader);
	}

	@Override
	public Plugin load(@NotNull File targetFile) throws PluginLoadException {
		try (JarFile jarFile = new JarFile(targetFile)) {
			JarEntry pluginConfigEntry = jarFile.getJarEntry("meta.json");
			if (pluginConfigEntry == null)
				throw new NullPointerException("File " + targetFile.getAbsolutePath() + " does not have a valid plugin config.");


			try (InputStream pluginConfigStream = jarFile.getInputStream(pluginConfigEntry)) {
				JSONObject pluginConfig = new JSONObject(IOUtils.toString(pluginConfigStream, "UTF-8"));
				if (pluginConfig.length() == 0)
					throw new NullPointerException("Plugin " + targetFile.getAbsolutePath() + " not supported.");
				if (!pluginConfig.has("plugin"))
					throw new NullPointerException("Plugin " + targetFile.getAbsolutePath() + " not supported.");
				pluginConfig = pluginConfig.getJSONObject("plugin");
				PluginType pluginType = PluginType.find(pluginConfig.optString("type")); // optional, because it will be checked later
				if (pluginType == null || !pluginTypeLoaders.containsKey(pluginType))
					throw new NullPointerException("Plugin " + targetFile.getAbsolutePath() + " not supported.");

				pluginConfigStream.close();
				jarFile.close();
				return pluginTypeLoaders.get(pluginType).load(targetFile);
			}
		} catch (PluginLoadException e) {
			throw e; // Avoid nesting
		} catch (Exception e) {
			throw new PluginLoadException(e);
		}
	}

	@Override
	public boolean unload(@NotNull Plugin targetPlugin) throws PluginUnloadException {
		try {
			if(!isLoaded(targetPlugin))
				throw new PluginUnloadException("Plugin not loaded.");
				PluginType type = targetPlugin.getPluginInfo().getPluginType();
			if (!pluginTypeLoaders.containsKey(type))
				throw new PluginNotSupportedException("Plugin with type " + type.name() + " cannot be unloaded by loader.");
			return pluginTypeLoaders.get(type).unload(targetPlugin);
		} catch (PluginUnloadException e) {
			throw e; // Avoid nesting
		} catch (Exception e) {
			throw new PluginUnloadException(e);
		}
	}

	public List<Plugin> getPlugins() {
		return plugins;
	}

	public boolean isLoaded(Plugin plugin) {
		return plugins.contains(plugin);
	}
}
