package net.scrumplex.vidlink.core.plugins;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface PluginLoader {

	List<Plugin> plugins = new ArrayList<>();

	Plugin load(@NotNull File targetFile) throws IOException, PluginLoadException;

	boolean unload(@NotNull Plugin targetPlugin) throws IOException, PluginUnloadException;

}
