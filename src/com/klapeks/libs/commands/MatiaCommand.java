package com.klapeks.libs.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.klapeks.libs.nms.NMS;

public abstract class MatiaCommand {
	
	protected static List<String> players = (List<String>)ImmutableList.<String>of("@@@@@@@@@@players@@@@@@@@@@@@@@");
	
	
	private final BukkitCommand bukkit;
	protected final String command;
	public MatiaCommand(String cmd) {
		this.command = cmd.contains(":")?cmd.substring(cmd.indexOf(":")+1):cmd;
		bukkit = new BukkitCommand(this.command, getDescription(), getUsage(), getAlias()) {
			@Override
			public boolean execute(CommandSender sender, String alias, String[] args) {
				if (sender instanceof ConsoleCommandSender) {
					MatiaCommand.this.onConsole((ConsoleCommandSender) sender, args);
					return true;
				}
				if (!(sender instanceof Player)) return true;
				MatiaCommand.this.onCommand((Player) sender, args);
				return true;
			}
			@Override
			public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
				if (!(sender instanceof Player)) return onTab(sender, args);
				List<String> list = MatiaCommand.this.onTab((Player) sender, args);
				if (list==null) list = (List<String>)ImmutableList.<String>of();
				else if (list==players) return super.tabComplete(sender, alias, args);
				return list;
			}
		};
		NMS.server.registerCommand(cmd.contains(":")?cmd.split(":")[0]:"klapeks", bukkit);
	}

	public abstract void onCommand(Player p, String[] args);
	public abstract List<String> onTab(Player p, String[] args);

	public List<String> onTab(CommandSender ccs, String[] args) {
		return (List<String>)ImmutableList.<String>of();
	}
	public void onConsole(CommandSender ccs, String[] args) {
		ccs.sendMessage("§cSorry. This command only for players");
	}
	
	public List<String> getAlias() {
		return new ArrayList<>();
	}
	public String noPermissions() {
		return "§cYou don't have perms";
	}
	public void noPermissions(Player p) {
		p.sendMessage(noPermissions());
	}
	public String getUsage() {
		return "§4[] §c/"+command;
	}
	public String getDescription() {
		return "Matia Command";
	}
//	
}
