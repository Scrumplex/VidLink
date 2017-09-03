package net.scrumplex.vidlink.core.plugins.java;

import net.scrumplex.vidlink.core.plugins.*;
import net.scrumplex.vidlink.core.plugins.java.platforms.PlatformPluginLoader;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JavaPluginLoader implements PluginLoader {

	protected final List<JavaPlugin> plugins = new ArrayList<>();
	private Map<PluginType, PluginLoader> pluginTypeLoaders;

	public JavaPluginLoader() {
		this(new HashMap<>());
	}

	public JavaPluginLoader(@NotNull Map<PluginType, PluginLoader> pluginTypeLoaders) {
		super();

		this.pluginTypeLoaders = pluginTypeLoaders;

		if(this.pluginTypeLoaders.isEmpty()) { // Add default values if none specified.
			putPluginTypeLoader(PluginType.PLATFORM, new PlatformPluginLoader(this));
			// More will follow
		}
	}

	public void putPluginTypeLoader(@NotNull PluginType type, @NotNull PluginLoader loader) {
		pluginTypeLoaders.put(type, loader);
	}

	@Override
	public Plugin load(@NotNull File targetFile) throws IOException, PluginLoadException {
		try (JarFile jarFile = new JarFile(targetFile)) {
			JarEntry pluginConfigEntry = jarFile.getJarEntry("meta.json");
			if (pluginConfigEntry == null)
				throw new NullPointerException("File " + targetFile.getAbsolutePath() + " does not have a valid plugin config.");


			try (InputStream pluginConfigStream = jarFile.getInputStream(pluginConfigEntry)) {
				JSONObject pluginConfig = new JSONObject(pluginConfigStream);
				PluginType pluginType = PluginType.find(pluginConfig.optString("plugin.type")); // optional, because it will be checked later
				if (pluginType == null || !pluginTypeLoaders.containsKey(pluginType))
					throw new NullPointerException("Plugin " + targetFile.getAbsolutePath() + " not supported.");

				pluginConfigStream.close();
				jarFile.close();
				return pluginTypeLoaders.get(pluginType).load(targetFile);
			}
		} catch (Exception e) {
			if (e instanceof PluginLoadException) // Avoid nesting
				throw e;

			throw new PluginLoadException(e);
		}
	}

	@Override
	public boolean unload(@NotNull Plugin targetPlugin) {
		return pluginTypeLoaders.get(targetPlugin.getPluginInfo().getPluginType()).unload(targetPlugin);
	}


	public List<JavaPlugin> getPlugins() {
		return plugins;
	}

	public boolean isLoaded(JavaPlugin plugin) {
		return plugins.contains(plugin);
	}
}
