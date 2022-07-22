package com.klapeks.libs.nms;

import org.bukkit.Bukkit;

public class NMS {

	public static final ServerNMS server = get(ServerNMS.class);
	public static final ItemNMS item = get(ItemNMS.class);
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	static <T> T get(Class<T> clazz) {
		String val = "com.klapeks.libs.nms."+getVersion()+"."+clazz.getSimpleName();
		Bukkit.getLogger().info("------  " + val);
		try {
			return (T) Class.forName(val).newInstance();
		} catch (Throwable t) {
			throw new RuntimeException("There is no " + clazz.getSimpleName() + " for " + getVersion(), t);
		}
	}

	static String version;
	public static String getVersion() {
		if (version==null) {
			String name = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",");
			version = name.substring(name.lastIndexOf(',') + 1);
		}
		return version;
	}
}
