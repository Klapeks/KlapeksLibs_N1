package com.klapeks.libs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;

public class MSG {

	public static void playSoundGlobal(Location loc, String sound, SoundCategory category) {
		Bukkit.getOnlinePlayers().forEach(pl -> {
			if (!pl.getWorld().equals(loc.getWorld())) return;
			pl.playSound(loc, sound, category, 1, 1);
		});
	}

	public static void log(String msg) {
		Bukkit.getConsoleSender().sendMessage(msg);
	}

	public static void broadcastPerms(String msg, String perms) {
		Bukkit.getOnlinePlayers().forEach(p -> {
			if (!p.hasPermission(perms)) return;
			p.sendMessage(msg);
		});
//		Bukkit.getLogger().info(perms);
		Bukkit.getConsoleSender().sendMessage(msg);
	}

	public static String join(String joining, Object... objs) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < objs.length; i++) {
			if (i>0) sb.append(joining);
			sb.append(objs[i]);
		}
		return sb.toString();
	}
}
