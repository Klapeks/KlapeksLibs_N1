package com.klapeks.libs.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.klapeks.libs.nms.NMS;

public class ComplexMatiaCommand extends SubCommand {
	private final BukkitCommand bukkit;
	String name;
	public ComplexMatiaCommand(String cmd) {
		super(cmd, (BiConsumer<CommandSender, String[]>) null, 0);
		this.name = cmd;
		if (cmd.contains(":")) {
			cmd = cmd.split("\\:")[0];
			this.name = this.name.substring(cmd.length()+1);
		} else cmd = "klapeks";
		
		bukkit = new BukkitCommand(this.name, getDescription(), getUsage(), new ArrayList<>()) {
			@Override
			public boolean execute(CommandSender sender, String alias, String[] args) {
				if (!proccess(sender, args)) {
					sender.sendMessage(getUsage());
				}
				return true;
			}
			@Override
			public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
				List<String> list = tab(sender, args);
				if (list==null) return (List<String>)ImmutableList.<String>of();
				else if (list==MatiaCommand.players) {
					return super.tabComplete(sender, alias, args);
				}
				return list;
			}
		};
		NMS.server.registerCommand(cmd, bukkit);
	}
	
	public void on(Consumer<CommandSender> c) {
		on((p, args) -> c.accept(p));
	}
	public void on(BiConsumer<CommandSender, String[]> c) {
		this.cmd = c;
	}
	
	void accept(Player p, String[] args) {
		cmd.accept(p, args);
	}
	public String getUsage() {
		return "§4[] §c/"+name;
	}
	public String getDescription() {
		return "Matia Command";
	}

}
