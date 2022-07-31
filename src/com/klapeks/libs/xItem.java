package com.klapeks.libs;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.klapeks.libs.commands.Messaging;

public class xItem {

	public static ItemStack of(Material mat, String title) {
		return of(mat, title, null);
	}
	public static ItemStack of(Material mat, String title, List<String> lore) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Messaging.msg(title));
		if (lore!=null) meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
}
