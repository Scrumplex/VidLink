package net.scrumplex.vidlink.core.plugins;

public class PluginNotSupportedException extends Exception {

	public PluginNotSupportedException(String cause) {
		super(cause);
	}

	public PluginNotSupportedException(String cause, Exception ex) {
		super(cause, ex);
	}

	public PluginNotSupportedException(Exception ex) {
		super(ex);
	}

}
