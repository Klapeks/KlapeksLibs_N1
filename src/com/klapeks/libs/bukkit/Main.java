package com.klapeks.libs.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.klapeks.libs.commands.Messaging;
import com.klapeks.libs.inv.CustomInventory;
import com.klapeks.libs.nms.NMS;

public class Main extends JavaPlugin implements Listener {
	
	public static Main plugin;
	public Main() {
		plugin = this;
	}
	@Override
	public void onEnable() {
		NMS.getVersion();
		getLogger().info(Messaging.msg("plugin.loaded"));
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{
			Player p = (Player) e.getWhoClicked();
			if (p.getOpenInventory()==null) return;
			p.getOpenInventory().setCursor(p.getOpenInventory().getCursor());
		});
		if (CustomInventory.doDrag(e)) return;
	}
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory()==null) return;
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{
			Player p = (Player) e.getWhoClicked();
			if (p.getOpenInventory()==null) return;
			p.getOpenInventory().setCursor(p.getOpenInventory().getCursor());
		});
		if (CustomInventory.doClick(e)) return;
	}
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (CustomInventory.doClose(e)) return;
	}
}
