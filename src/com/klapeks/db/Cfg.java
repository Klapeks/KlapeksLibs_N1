package com.klapeks.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.klapeks.libs.Main;

public class Cfg {

	public static FileConfiguration cfg(File file) {
		return cfg(file, null);
	}
	public static FileConfiguration cfg(String path) {
		return cfg(new File(path), null);
	}
	public static FileConfiguration cfg(String path, BiConsumer<FileConfiguration, File> onCreate) {
		File file = new File(path);
		return cfg(file, (cfg) -> onCreate.accept(cfg, file));
	}
	public static FileConfiguration cfg(File file, Consumer<FileConfiguration> onCreate) {
		if (file.exists()) {
			return YamlConfiguration.loadConfiguration(file);
		}
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if (onCreate!=null) onCreate.accept(cfg);
		return cfg;
	}
	public static File getLibDataFolder() {
		return Main.plugin.getDataFolder();
	}
	public static void extractResource(String file) {
		Main.plugin.saveResource(file, false);
	}
	
	public static Object[] toArray(List<Object> list){
		Object[] ob = new Object[list.size()];
		for (int i = list.size()-1;i>=0;i--) {
			ob[i] = list.get(i);
		}
		return ob;
	}
	public static List<Object> toList(Object[] objs) {
		List<Object> list = new ArrayList<>();
		for (Object o : objs) {
			list.add(o);
		}
		return list;
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
