package com.klapeks.libs.nms.v1_16_R3;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.scoreboard.CraftScoreboard;
import org.bukkit.scoreboard.Scoreboard;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.World;

public class ServerNMS implements com.klapeks.libs.nms.ServerNMS {

	@Override
	public void broadcastChestAnimation(Location loc, boolean isOpened) {
		World world = ((CraftWorld) loc.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
        world.playBlockAction(position, world.getType(position).getBlock(), 1, isOpened ? 1 : 0);
	}

	@Override
	public boolean isMainScoreboard(Scoreboard scoreboard) {
		Scoreboard main = Bukkit.getScoreboardManager().getMainScoreboard();
		if (scoreboard.equals(main)) return true;
		return ((CraftScoreboard) scoreboard).getHandle().equals(((CraftScoreboard) main).getHandle());
	}

	@Override
	public void registerCommand(String prefix, BukkitCommand cmd) {
		((CraftServer) Bukkit.getServer()).getCommandMap().register(prefix, cmd);
	}
	
}
