package net.scrumplex.vidlink.core.plugins.java.platforms;

import net.scrumplex.vidlink.core.plugins.Plugin;
import net.scrumplex.vidlink.core.plugins.PluginLoadException;
import net.scrumplex.vidlink.core.plugins.PluginLoader;
import net.scrumplex.vidlink.core.plugins.PluginType;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PlatformPluginLoader implements PluginLoader {

	private final Map<PlatformPlugin, PlatformPluginClassLoader> classloaders = new HashMap<>();
	private final PluginLoader parentPluginLoader;

	public PlatformPluginLoader(PluginLoader parentPluginLoader) {
		this.parentPluginLoader = parentPluginLoader;
	}

	@Override
	public PlatformPlugin load(@NotNull File targetFile) throws IOException, PluginLoadException {
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
				if (pluginType != PluginType.PLATFORM)
					throw new NullPointerException("Plugin " + targetFile.getAbsolutePath() + " not supported by loader.");
				String pluginMain = pluginConfig.getString("main");
				String pluginName = pluginConfig.getString("name");
				String pluginAuthor = pluginConfig.optString("author");
				String pluginVersion = pluginConfig.optString("version");

				PlatformPluginInfo pluginInfo = new PlatformPluginInfo(targetFile, pluginType, pluginMain, pluginName, pluginAuthor, pluginVersion);

				PlatformPluginClassLoader loader = new PlatformPluginClassLoader(getClass().getClassLoader(), pluginInfo);
				PlatformPlugin platformPlugin = loader.getPlugin();

				platformPlugin.initialize(pluginInfo);

				parentPluginLoader.plugins.add(platformPlugin);
				classloaders.put(platformPlugin, loader);

				return platformPlugin;
			}
		} catch (Exception e) {
			throw new PluginLoadException(e);
		}
	}

	@Override
	public boolean unload(@NotNull Plugin targetPlugin) throws IOException {
		if (targetPlugin instanceof PlatformPlugin && plugins.contains(targetPlugin)) {
			PlatformPlugin plugin = (PlatformPlugin) targetPlugin;
			plugin.unload();
			plugins.remove(targetPlugin);
			PlatformPluginClassLoader pluginClassLoader = classloaders.remove(plugin);
			pluginClassLoader.disable();
			return true;

		}
		return false;
	}

}
