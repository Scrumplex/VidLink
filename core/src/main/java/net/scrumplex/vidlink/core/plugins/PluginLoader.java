package net.scrumplex.vidlink.core.plugins;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public interface PluginLoader {

	Plugin load(@NotNull File targetFile) throws IOException, PluginLoadException;

	boolean unload(@NotNull Plugin targetPlugin);

}
