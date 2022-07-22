package com.klapeks.libs.commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Messaging {
	static FileConfiguration cfg;
	static {
		File file = new File("plugins/KlapeksLib/lang.yml");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			cfg = YamlConfiguration.loadConfiguration(file);
			def("region.flying_disabled", "&cВ этом регионе запрещено летать!");
			def("plugin.loaded", "&aПлагин был успешно запущен");
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else cfg = YamlConfiguration.loadConfiguration(file);
	}
	
	private static void def(String key, String msg) {
		if (cfg.contains(key)) return;
		cfg.set(key, msg);
	}

	public static void msg(Player p, String msg) {
		p.sendMessage(format(p, msg));
	}
	public static String msg(String msg) {
		if (cfg.contains(msg)) msg = cfg.getString(msg);
		msg = msg.replace("&", "§");
		if (msg.contains("§#")) {
			String[] aa = msg.split("§#");
			StringBuilder sb = new StringBuilder();
			sb.append(aa[0]);
			for (int i = 1; i<aa.length;i++) {
				if (aa[i].length()<6) {
					sb.append("#");
					sb.append(aa[i]);
					continue;
				}
				sb.append(toColor(aa[i].substring(0, 6), aa[i].substring(6)));
			}
			msg = sb.toString();
		}
		return msg;
	}
	public static String format(Player p, String msg) {
		return msg(msg);
	}
	
	public static String toColor(String hex, String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("§x");
		for (String s : hex.split("")) {
			sb.append("§");
			sb.append(s);
		}
		sb.append(msg);
		return sb.toString();
	}
}
