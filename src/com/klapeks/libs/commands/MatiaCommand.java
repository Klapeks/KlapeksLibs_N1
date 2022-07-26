package com.klapeks.libs.commands;

import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.klapeks.libs.uUtils;
import com.klapeks.libs.exceptions.NoCatchException;
import com.klapeks.libs.exceptions.NoPermsException;
import com.klapeks.libs.nms.NMS;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod({uUtils.class})
public abstract class MatiaCommand {
	
	protected static List<String> players = (List<String>)ImmutableList.<String>of("@@@@@@@@@@@@@@players@@@@@@@@@@@@@@");
	
	private final BukkitCommand bukkit;
	protected final String command;
	public MatiaCommand(String cmd, String... alias) {
		this.command = cmd.contains(":")?cmd.substring(cmd.indexOf(":")+1):cmd;
		bukkit = new BukkitCommand(this.command, getDescription(), getHelpUsage(), alias.asList()) {
			@Override
			public boolean execute(CommandSender sender, String alias, String[] args) {
				try {
					command(sender, args);
				} catch (NoCatchException e) {
					sender.sendMessage("�c"+e.getMessage());
				}
				return true;
			}
			@Override
			public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
				Collection<String> cl = tab(sender, args);
				if (cl==null) return (List<String>)ImmutableList.<String>of();
				else if (cl==players) return super.tabComplete(sender, alias, args);
				return cl.filter(s -> s.startsWithIgnoreCase(args.last()));
			}
		};
		NMS.server.registerCommand(cmd.contains(":")?cmd.split(":")[0]:"klapeks", bukkit);
	}

	public void command(CommandSender sender, String[] args) {
		validPlayer(sender);
		onCommand((Player) sender, args);
	}
	public Collection<String> tab(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) return null;
		return onTab((Player) sender, args);
	}

	public void onCommand(Player p, String[] args) {}
	public Collection<String> onTab(Player p, String[] args){return null;}
//
//	public List<String> onTab(CommandSender ccs, String[] args) {
//		return (List<String>)ImmutableList.<String>of();
//	}
//	public void onConsole(CommandSender ccs, String[] args) {
//		ccs.sendMessage("�cSorry. This command only for players");
//	}
//	
//	public String noPermissions() {
//		return "�cYou don't have perms";
//	}
//	public void noPermissions(Player p) {
//		p.sendMessage(noPermissions());
//	}
	public String getHelpUsage() {
		return ">_<";
	}
	public String getDescription() {
		return "Matia Command";
	}
	
	public static void validPerms(CommandSender sender, String perms) {
		if (sender.hasPermission(perms)) return;
		throw new NoPermsException(perms);
	}

	public static void validUsage(boolean expression, String error) {
		if (expression) return;
		throw new NoCatchException(error);
	}
	public static void validPlayer(Object object) {
		if (object instanceof Player) return;
		throw new NoCatchException("This command only for players :(");
	}
//	
}
