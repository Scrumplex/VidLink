package net.scrumplex.vidlink.core.plugins;

/**
 * Created by Sefa Eyeoglu on 03.09.17.
 */
public enum PluginType {

	PLATFORM,
	FRONTEND;

	public static PluginType find(String name) {
		for (PluginType type : PluginType.values()) {
			if(type.name().equalsIgnoreCase(name))
				return type;
		}
		return null;
	}

	public static boolean exists(String name) {
		return find(name) != null;
	}

}
