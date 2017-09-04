package net.scrumplex.vidlink.core.plugins.java.platforms;

import net.scrumplex.vidlink.core.plugins.PluginLoadException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class PlatformPluginClassLoader extends URLClassLoader {


	private final PlatformPlugin plugin;
	private final Map<String, Class<?>> classes;

	private boolean disabled = false;

	PlatformPluginClassLoader(ClassLoader parent, PlatformPluginInfo pluginInfo) throws MalformedURLException, IllegalAccessException, InstantiationException, PluginLoadException {
		super(new URL[]{pluginInfo.getPluginFile().toURI().toURL()}, parent);
		this.classes = new HashMap<>();

		Class<?> jarClass;

		try {
			jarClass = Class.forName(pluginInfo.getMainClass(), true, this);
		} catch (ClassNotFoundException ex) {
			throw new PluginLoadException("Cannot find main class " + pluginInfo.getMainClass(), ex);
		}

		Class<? extends PlatformPlugin> pluginClass;
		try {
			pluginClass = jarClass.asSubclass(PlatformPlugin.class);
		} catch (ClassCastException ex) {
			throw new PluginLoadException("Abstract main class " + pluginInfo.getMainClass(), ex);
		}
		classes.put(pluginInfo.getMainClass(), pluginClass);
		plugin = pluginClass.newInstance();
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (this.disabled)
			throw new ClassNotFoundException(name);
		if (name.startsWith("net.scrumplex.vidlink.core."))
			throw new ClassNotFoundException(name);

		Class<?> clazz = classes.get(name);

		if (clazz == null) {
			clazz = super.findClass(name);
			if (clazz != null) {
				classes.put(name, clazz);
			}
		}

		return clazz;
	}

	void disable() throws IOException {
		this.disabled = true;
		this.close();
	}

	PlatformPlugin getPlugin() {
		return this.plugin;
	}
}
