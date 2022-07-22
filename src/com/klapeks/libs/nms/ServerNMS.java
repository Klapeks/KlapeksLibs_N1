package com.klapeks.libs.nms;

import org.bukkit.Location;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;

public interface ServerNMS {

	public void registerCommand(String prefix, BukkitCommand cmd);
	public default void registerCommand(BukkitCommand cmd) {
		registerCommand("klapeks", cmd);
	}
	public default void regsiterCommand(Plugin plugin, BukkitCommand cmd) {
		registerCommand(plugin.getName().toLowerCase(), cmd);
	}
	public void broadcastChestAnimation(Location loc, boolean isOpened);
	public boolean isMainScoreboard(Scoreboard scoreboard);
	
}
