package net.scrumplex.vidlink.core;

import net.scrumplex.vidlink.core.plugins.Plugin;
import net.scrumplex.vidlink.core.plugins.PluginLoadException;
import net.scrumplex.vidlink.core.plugins.PluginUnloadException;
import net.scrumplex.vidlink.core.plugins.java.JavaPluginLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// TODO: Load plugins

		File targetDirectory = new File("plugins");
		if(!targetDirectory.exists())
			targetDirectory.mkdir();

		JavaPluginLoader loader = new JavaPluginLoader();

		for(File file : targetDirectory.listFiles()) {
			if(file.getName().endsWith(".jar")) {
				try {
					loader.load(file);
				} catch (PluginLoadException e) {
					e.printStackTrace();
				}
			}
		}
		List<Plugin> plugins = new ArrayList<>(loader.getPlugins());
		for (Plugin plugin : plugins) {
			try {
				loader.unload(plugin);
			} catch (PluginUnloadException e) {
				e.printStackTrace();
			}
		}
	}
}
