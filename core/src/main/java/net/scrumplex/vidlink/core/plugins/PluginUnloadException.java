package net.scrumplex.vidlink.core.plugins;

public class PluginUnloadException extends Exception {

	public PluginUnloadException(String cause) {
		super(cause);
	}

	public PluginUnloadException(String cause, Exception ex) {
		super(cause, ex);
	}

	public PluginUnloadException(Exception ex) {
		super(ex);
	}

}
