package com.klapeks.libs.gui;

import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIs {

	public static int getPos(int x, int y) {
		return 9*(y-1)-1+x;
	}

	public static void setItem(Inventory inv, int x, int y, ItemStack item) {
		inv.setItem(getPos(x,y), item);
	}
	public static void setItem(Inventory inv, int slot, ItemStack item) {
		inv.setItem(slot, item);
	}

	public static void setDecor(ItemStack item, String title) {
		setDecor(item, title, null);
	}
	public static void setDecor(ItemStack item, String title, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(title);
		if (lore!=null) meta.setLore(lore);
		item.setItemMeta(meta);
	}
}
