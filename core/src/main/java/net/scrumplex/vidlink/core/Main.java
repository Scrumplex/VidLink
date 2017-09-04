package net.scrumplex.vidlink.core;

import net.scrumplex.vidlink.core.plugins.PluginLoadException;
import net.scrumplex.vidlink.core.plugins.java.JavaPluginLoader;

import java.io.File;
import java.io.IOException;

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
				} catch (IOException e) {
					e.printStackTrace();
				} catch (PluginLoadException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
