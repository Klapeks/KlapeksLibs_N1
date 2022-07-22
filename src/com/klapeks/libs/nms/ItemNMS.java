package com.klapeks.libs.nms;

import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

public interface ItemNMS {

	@Nullable
	public Map<String, Object> getNBT(ItemStack item);
	@Nullable
	public <T extends Object> T getNBT(ItemStack item, String key);
	@Nullable
	public <T extends Object> T getNBT(ItemStack item, String key, Class<T> clazz);
	
	public boolean hasNBT(ItemStack item, String key);

	public ItemStack setNBT(ItemStack item, Map<String, Object> map);
	public ItemStack addNBT(ItemStack item, Map<String, Object> map);
	public ItemStack setNBT(ItemStack item, String key, Object value);
	
}
