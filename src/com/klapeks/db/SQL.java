package com.klapeks.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.klapeks.libs.Main;

public class SQL {
	
	static Connection connection;
	
	static String type;
	public static String getType() {
		return type;
	}
	public static boolean isSQLType() {
		return true;
	}
	
	public static Connection sql() {
		if (connection==null) connect();
		return connection;
	}

	public static PreparedStatement sql(String sql, Object... args) throws SQLException {
		 PreparedStatement statement = sql().prepareStatement(sql);
		 for (int i = 0; i < args.length; i++) {
			 statement.setObject(i+1, args[i]);
		 }
		 return statement;
	}
	
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
		case "mysql": {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.err.println("Can't find MySQL Driver");
				throw new RuntimeException(e);
			}
			url = "jdbc:mysql://";
			break;
		}
		default: {
			throw new RuntimeException("Unknown database type: " + type);
		}
		}
		Properties properties = new Properties();
		properties.setProperty("user", cfg.getString("username"));
		properties.setProperty("password", cfg.getString("password"));
		properties.setProperty("characterEncoding", "utf8");
		url += cfg.get("host") + ":" + cfg.get("port")+"/";
		url += cfg.get("name");
		Bukkit.getLogger().info("Trying to connect to database with url " + url);
		try {
			connection = DriverManager.getConnection(url, properties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void disconnect() {
		if (connection == null) return;
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection = null;
	}
	
}
