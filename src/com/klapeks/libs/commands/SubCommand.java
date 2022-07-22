package com.klapeks.libs.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.entity.Player;

public class SubCommand {
	String name;
	BiConsumer<Player, String[]> cmd;
	final int deep;
	
	SubCommand(String sub, Consumer<Player> c, int deep) {
		this(sub, (p, args) -> c.accept(p), deep); 
	}
	SubCommand(String sub, BiConsumer<Player, String[]> c, int deep) {
		this.name = sub;
		this.cmd = c;
		this.deep = deep;
//		if (complex==null && this instanceof ComplexMatiaCommand) {
//			complex = (ComplexMatiaCommand) this;
//		}
//		this.complexCMD = complex;
	}
	
	public SubCommand on(String sub) {
		return on(sub, (BiConsumer<Player, String[]>) null);
	}
	
	List<SubCommand> subs = null;
	SubCommand argsCommand = null;
	public SubCommand on(String sub, BiConsumer<Player, String[]> c) {
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
	public SubCommand on(String sub, Consumer<Player> c) {
		return on(sub, (p,args) -> c.accept(p));
	}
	public SubCommand on(String sub, String wrong) {
		return on(sub, (p, args) -> p.sendMessage(wrong));
	}

	public SubCommand onArgument(BiConsumer<Player, String> c) {
		return on("<arg>", (p, args) -> {
			c.accept(p, args[deep]);
		});
	}
	public SubCommand onPlayer(BiConsumer<Player, String> c) {
		return on("<player>", (p, args) -> {
			c.accept(p, args[deep]);
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
	public boolean hasPerms(Player p) {
		try {
			if (p.isOp()) return true;
			return p.hasPermission(perms);	
		} catch (Throwable t) {
			return false;
		}
	}
	protected List<String> tab(Player p, String[] args) {
		if (!hasPerms(p)||name.equals("<endargs>")) {
			return null;
		}
		if (args==null||args.length-1==deep) {
			if (argsCommand!=null) {
				if (argsCommand.name.equals("<player>")) {
					return MatiaCommand.players;
				}
				return argsCommand.tab(p, args);
			}
			if (subs==null) return null;
			List<String> list = new ArrayList<>();
			for (SubCommand s : subs) {
				if (s.hasPerms(p)) {
					list.add(s.name);
				}
			}
			return list;
		}
		if (argsCommand!=null) {
			return argsCommand.tab(p, args);
		}
		if (subs==null) return null;
		for (SubCommand s : subs) {
			if (s.isWordAccepted(args[deep])) {
				return s.tab(p, args);
			}
		}
		return null;
	}
	protected boolean proccess(Player p, String[] args) {
		if (args==null||args.length==deep||name.equals("<endargs>")) {
			if (cmd==null) return false;
			cmd.accept(p, args);
			return true;
		}
		if (argsCommand!=null) {
			if (argsCommand.isWordAccepted(args[deep])) {
				return argsCommand.proccess(p, args);
			}
		}
		if (subs==null) {
			return false;
		}
		for (SubCommand s : subs) {
			if (s.isWordAccepted(args[deep])) {
				return s.proccess(p, args);
			}
		}
		return false;
	}
}
