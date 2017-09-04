package net.scrumplex.vidlink.core.plugins.java;

import net.scrumplex.vidlink.core.plugins.Plugin;
import net.scrumplex.vidlink.core.plugins.PluginInfo;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Sefa Eyeoglu on 03.09.17.
 */
public class JavaPlugin implements Plugin {

	private PluginInfo info;

	public JavaPlugin() {
	}

	public void initialize(@NotNull PluginInfo info) {
		this.info = info;
		onEnable();
	}

	public final void unload() {
		onDisable();
		info = null;
	}

	protected void onEnable() {

	}

	protected void onDisable() {

	}

	@Override
	public PluginInfo getPluginInfo() {
		return info;
	}
}
