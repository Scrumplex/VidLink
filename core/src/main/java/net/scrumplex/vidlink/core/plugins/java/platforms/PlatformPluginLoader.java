package net.scrumplex.vidlink.core.plugins.java.platforms;

import net.scrumplex.vidlink.core.plugins.PluginLoadException;
import net.scrumplex.vidlink.core.plugins.java.JavaPluginLoader;
import net.scrumplex.vidlink.core.plugins.PluginLoader;
import net.scrumplex.vidlink.core.plugins.PluginType;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PlatformPluginLoader extends JavaPluginLoader {

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
				JSONObject pluginConfig = new JSONObject(pluginConfigStream);
				PluginType pluginType = PluginType.find(pluginConfig.getString("plugin.type")); // cannot be null. (Except if the json file changes magically before loading)
				String pluginMain = pluginConfig.getString("plugin.main");
				String pluginName = pluginConfig.getString("plugin.name");
				String pluginAuthor = pluginConfig.optString("plugin.author");
				String pluginVersion = pluginConfig.optString("plugin.version");

				PlatformPluginInfo pluginInfo = new PlatformPluginInfo(targetFile, pluginType, pluginMain, pluginName, pluginAuthor, pluginVersion);

				PlatformPluginClassLoader loader = new PlatformPluginClassLoader(getClass().getClassLoader(), pluginInfo);
				PlatformPlugin platformPlugin = loader.getPlugin();

				platformPlugin.initialize(pluginInfo);

				plugins.add(platformPlugin);
				classloaders.put(platformPlugin, loader);

				return platformPlugin;
			}
		} catch (Exception e) {
			throw new PluginLoadException(e);
		}
	}

}
