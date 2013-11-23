package com.yayestechlab.bungeecord.Y23KCoreBungee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import net.craftminecraft.bungee.bungeeyaml.bukkitapi.file.FileConfiguration;
import net.craftminecraft.bungee.bungeeyaml.pluginapi.ConfigurablePlugin;

public class Main extends ConfigurablePlugin {
	
	private FileConfiguration config;
	
	public void onEnable() {
		config = getConfig();
	}

	public Connection getConnection() throws SQLException {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", config.getString("mysql.username"));
		connectionProps.put("password", config.getString("mysql.password"));

		conn = DriverManager.getConnection(
				"jdbc:mysql://" + config.getString("mysql.address") + "/" + config.getString("mysql.schema"),
				connectionProps);
		System.out.println("Connected to database");
		return conn;
	}
}
