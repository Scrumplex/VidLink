package net.scrumplex.vidlink.core.plugins;

public class PluginLoadException extends Exception {

	public PluginLoadException(String cause) {
		super(cause);
	}

	public PluginLoadException(String cause, Exception ex) {
		super(cause, ex);
	}

	public PluginLoadException(Exception ex) {
		super(ex);
	}

}
