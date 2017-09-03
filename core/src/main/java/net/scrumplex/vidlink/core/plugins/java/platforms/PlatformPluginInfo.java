package net.scrumplex.vidlink.core.plugins.java.platforms;

import net.scrumplex.vidlink.core.plugins.java.JavaPluginInfo;
import net.scrumplex.vidlink.core.plugins.PluginType;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class PlatformPluginInfo extends JavaPluginInfo {

	public PlatformPluginInfo(@NotNull File file, @NotNull PluginType type, @NotNull String mainClass, @NotNull String name, @NotNull String author, @NotNull String version) {
		super(file, type, mainClass, name, author, version);
	}
}
