package com.klapeks.libs;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.klapeks.libs.commands.Messaging;

public class xItem {

	public static ItemStack of(Material mat, String title) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Messaging.msg(title));
		item.setItemMeta(meta);
		return item;
	}
	
}
