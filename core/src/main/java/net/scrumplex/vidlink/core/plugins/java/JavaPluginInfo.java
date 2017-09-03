package net.scrumplex.vidlink.core.plugins.java;

import net.scrumplex.vidlink.core.plugins.PluginInfo;
import net.scrumplex.vidlink.core.plugins.PluginType;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by Sefa Eyeoglu on 03.09.17.
 */
public class JavaPluginInfo implements PluginInfo {

	private final PluginType type;
	private final String name;
	private final String author;
	private final String version;
	private final File file;
	private final String mainClass;

	public JavaPluginInfo(@NotNull File file, @NotNull PluginType type, @NotNull String mainClass, @NotNull String name, @NotNull String author, @NotNull String version) {
		this.type = type;
		this.name = name;
		this.author = author;
		this.version = version;
		this.file = file;
		this.mainClass = mainClass;
	}

	@Override
	public PluginType getPluginType() {
		return type;
	}

	@Override
	public File getPluginFile() {
		return file;
	}

	public String getMainClass() {
		return mainClass;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public String getVersion() {
		return version;
	}
}
