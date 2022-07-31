package com.klapeks.libs.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.klapeks.libs.MSG;

public class Messaging {
	static FileConfiguration cfg;
	static {
		File file = new File("plugins/KlapeksLibs/lang.yml");
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
		if (msg.contains("&") || msg.contains("§")) {}
		else if (cfg.contains(msg)) msg = cfg.getString(msg);
		msg = msg.replace("&", "§");
		if (!msg.contains("§#")) return msg;
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
	
	public static String repeat(String str, int times) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < times; i++) {
			sb.append(str);
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public static <T> List<T> listOf(T... str){
		return MSG.listOf(str);
	}
}
