package com.klapeks.libs.nms.v1_18_R2;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.scoreboard.CraftScoreboard;
import org.bukkit.scoreboard.Scoreboard;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.World;

public class ServerNMS implements com.klapeks.libs.nms.ServerNMS {

	@Override
	public void broadcastChestAnimation(Location loc, boolean isOpened) {
		World world = ((CraftWorld) loc.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
        world.a(position, world.a_(position).b(), 1, isOpened ? 1 : 0);
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
