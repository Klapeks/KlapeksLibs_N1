package com.klapeks.db;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.klapeks.sql.Database;
import com.klapeks.sql.MatSQL;
import com.klapeks.sql.MatYML;
import com.klapeks.sql.Where;

public class SQL {
	
	static Database sql;
	
	static String type;
	public static String getType() {
		return type;
	}
	public static boolean isSQLType() {
		return sql instanceof MatSQL;
	}
	
	public static Database sql() {
		if (sql==null) connect();
		return sql;
	}
	public static Where where(String query, Object... placeholders) {
		return new Where(query, placeholders);
	}
//	public static PreparedStatement sql(String sql, Object... args) throws SQLException {
//		 PreparedStatement statement = sql().prepareStatement(sql);
//		 for (int i = 0; i < args.length; i++) {
//			 statement.setObject(i+1, args[i]);
//		 }
//		 return statement;
//	}
	
	public static void connect() {
		disconnect();
		File file = new File(Cfg.getLibDataFolder(), "database.yml");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			Cfg.extractResource("database.yml");
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		type = cfg.getString("type");
		String url;
		switch (type.toLowerCase()) {
		case "yaml":
		case "yml": {
			url = "km:yaml://";
			sql = new MatYML();
			break;
		}
		case "mysql": {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.err.println("Can't find MySQL Driver");
				throw new RuntimeException(e);
			}
			url = "jdbc:mysql://";
			sql = new MatSQL();
			break;
		}
		default: {
			throw new RuntimeException("Unknown database type: " + type);
		}
		}
		url += cfg.get("host") + ":" + cfg.get("port")+"/";
		url += cfg.get("name");
		Bukkit.getLogger().info("Trying to connect to database with url " + url);
		sql.connect(url, cfg.getString("username"), cfg.getString("password"));
	}
	public static void disconnect() {
		if (sql == null) return;
		sql.disconnect();
		sql = null;
	}
	
}
