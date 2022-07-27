package com.klapeks.libs.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SubCommand {
	String name;
	BiConsumer<CommandSender, String[]> cmd;
	final int deep;
	
	SubCommand(String sub, Consumer<CommandSender> c, int deep) {
		this(sub, (p, args) -> c.accept(p), deep); 
	}
	SubCommand(String sub, BiConsumer<CommandSender, String[]> c, int deep) {
		this.name = sub;
		this.cmd = c;
		this.deep = deep;
//		if (complex==null && this instanceof ComplexMatiaCommand) {
//			complex = (ComplexMatiaCommand) this;
//		}
//		this.complexCMD = complex;
	}
	
	public SubCommand on(String sub) {
		return on(sub, (BiConsumer<CommandSender, String[]>) null);
	}
	
	List<SubCommand> subs = null;
	SubCommand argsCommand = null;
	public SubCommand on(String sub, BiConsumer<CommandSender, String[]> c) {
		if (argsCommand!=null) throw new RuntimeException("Can't add subcommand to subcommand with args");
		if (sub.startsWith("<") && sub.endsWith(">")) {
			argsCommand = new SubCommand(sub, c, deep+1);
			return argsCommand;
		}
		SubCommand sc = new SubCommand(sub, c, deep+1);
		if (subs==null) subs = new ArrayList<>();
		subs.add(sc);
		return sc;
	}
	public SubCommand on(String sub, Consumer<CommandSender> c) {
		return on(sub, (s,args) -> c.accept(s));
	}
	public SubCommand on(String sub, String wrong) {
		return on(sub, (s, args) -> s.sendMessage(wrong));
	}

	public SubCommand onArgument(BiConsumer<CommandSender, String> c) {
		return on("<arg>", (s, args) -> {
			c.accept(s, args[deep]);
		});
	}
	public SubCommand onPlayer(BiConsumer<CommandSender, String> c) {
		return on("<player>", (s, args) -> {
			c.accept(s, args[deep]);
		});
	}
	
	public SubCommand onp(String sub, Consumer<Player> c) {
		return onp(sub, (s,args) -> c.accept(s));
	}
	public SubCommand onp(String sub, BiConsumer<Player, String[]> c) {
		return on(sub, (s,args) -> {
			if (s instanceof Player) c.accept((Player)s, args);
			else s.sendMessage("§cThis subcommand only for players");
		});
	}
	protected boolean isWordAccepted(String word) {
		if (name.equals("<arg>")) return true;
		if (name.equals("<player>")) return true;
		return name.equals(word);
	}
	
	String perms;
	public SubCommand setMinStatus(String perms) {
		this.perms = perms;
		return this;
	}
	public boolean hasPerms(CommandSender sender) {
		if (sender instanceof ConsoleCommandSender) return true;
		try {
			if (sender.isOp()) return true;
			return sender.hasPermission(perms);	
		} catch (Throwable t) {
			return false;
		}
	}
	protected List<String> tab(CommandSender sender, String[] args) {
		if (!hasPerms(sender)||name.equals("<endargs>")) {
			return null;
		}
		if (args==null||args.length-1==deep) {
			if (argsCommand!=null) {
				if (argsCommand.name.equals("<player>")) {
					return MatiaCommand.players;
				}
				return argsCommand.tab(sender, args);
			}
			if (subs==null) return null;
			List<String> list = new ArrayList<>();
			for (SubCommand s : subs) {
				if (s.hasPerms(sender)) {
					list.add(s.name);
				}
			}
			return list;
		}
		if (argsCommand!=null) {
			return argsCommand.tab(sender, args);
		}
		if (subs==null) return null;
		for (SubCommand s : subs) {
			if (s.isWordAccepted(args[deep])) {
				return s.tab(sender, args);
			}
		}
		return null;
	}
	protected boolean proccess(CommandSender sender, String[] args) {
		if (args==null||args.length==deep||name.equals("<endargs>")) {
			if (cmd==null) return false;
			cmd.accept(sender, args);
			return true;
		}
		if (argsCommand!=null) {
			if (argsCommand.isWordAccepted(args[deep])) {
				return argsCommand.proccess(sender, args);
			}
		}
		if (subs==null) {
			return false;
		}
		for (SubCommand s : subs) {
			if (s.isWordAccepted(args[deep])) {
				return s.proccess(sender, args);
			}
		}
		return false;
	}
}
