package com.klapeks.libs.nms.v1_16_R3;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTNumber;
import net.minecraft.server.v1_16_R3.NBTTagByte;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagDouble;
import net.minecraft.server.v1_16_R3.NBTTagFloat;
import net.minecraft.server.v1_16_R3.NBTTagInt;
import net.minecraft.server.v1_16_R3.NBTTagIntArray;
import net.minecraft.server.v1_16_R3.NBTTagLong;
import net.minecraft.server.v1_16_R3.NBTTagShort;
import net.minecraft.server.v1_16_R3.NBTTagString;

public class ItemNMS implements com.klapeks.libs.nms.ItemNMS {

	@Override
	public Map<String, Object> getNBT(ItemStack item) {
		if (item==null) return null;
		net.minecraft.server.v1_16_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
		if (!nms.hasTag()) return null;
		NBTTagCompound tag = nms.getTag();
		return toMap(tag);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getNBT(ItemStack item, String key) {
		if (item==null) return null;
		net.minecraft.server.v1_16_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
		if (!nms.hasTag()) nms.setTag(new NBTTagCompound());
		NBTTagCompound tag = nms.getTag();
		if (!tag.hasKey(key)) return null;
		return (T) toObj(tag.get(key));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getNBT(ItemStack item, String key, Class<T> clazz) {
		if (item==null) return null;
		net.minecraft.server.v1_16_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
		if (!nms.hasTag()) nms.setTag(new NBTTagCompound());
		NBTTagCompound tag = nms.getTag();
		if (!tag.hasKey(key)) return null;
		if (String.class.equals(clazz)) return (T) tag.get(key).asString();
		return (T) toObj(tag.get(key));
	}



	@Override
	public ItemStack setNBT(ItemStack item, Map<String, Object> map) {
		if (item==null) return item;
		net.minecraft.server.v1_16_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = (NBTTagCompound) toBase(map);
		nms.setTag(tag);
		return CraftItemStack.asBukkitCopy(nms);
	}

	@Override
	public ItemStack addNBT(ItemStack item, Map<String, Object> map) {
		if (item==null) return item;
		net.minecraft.server.v1_16_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
		if (!nms.hasTag()) nms.setTag(new NBTTagCompound());
		NBTTagCompound tag = nms.getTag();
		for (Object key : map.keySet()) {
			tag.set(key.toString(), toBase(map.get(key)));
		}
		nms.setTag(tag);
		return CraftItemStack.asBukkitCopy(nms);
	}
	@Override
	public ItemStack setNBT(ItemStack item, String key, Object value) {
		if (item==null) return item;
		net.minecraft.server.v1_16_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
		if (!nms.hasTag()) nms.setTag(new NBTTagCompound());
		NBTTagCompound tag = nms.getTag();
		tag.set(key, toBase(value));
		nms.setTag(tag);
		return CraftItemStack.asBukkitCopy(nms);
	}
	
	@Override
	public boolean hasNBT(ItemStack item, String key) {
		if (item==null) return false;
		net.minecraft.server.v1_16_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
		if (!nms.hasTag()) return false;
		return nms.getTag().hasKey(key);
	}

	
	private NBTBase toBase(Object object) {
		if (object==null) return null;
		if (object instanceof NBTBase) return (NBTBase) object;
		if (object instanceof Map<?,?>) {
			Map<?,?> map = (Map<?,?>) object;
			NBTTagCompound tag = new NBTTagCompound();
			for (Object key : map.keySet()) {
				tag.set(key.toString(), toBase(map.get(key)));
			}
			return tag;
		}
		if (object instanceof Byte) return NBTTagByte.a((byte) object);
		if (object instanceof Short) return NBTTagShort.a((short) object);
		if (object instanceof Integer) return NBTTagInt.a((int) object);
		if (object instanceof Long) return NBTTagLong.a((long) object);
		if (object instanceof Float) return NBTTagFloat.a((long) object);
		if (object instanceof Double) return NBTTagDouble.a((long) object);
		if (object instanceof String) return NBTTagString.a((String) object);
		return NBTTagString.a(object+"");
	}
	
	public Object toObj(NBTBase base) {
		if (base==null) return null;
		if (base instanceof NBTNumber) {
			return ((NBTNumber) base).k();
		}
		if (base instanceof NBTTagIntArray) return ((NBTTagIntArray) base).getInts();
		if (base instanceof NBTTagCompound) return toMap((NBTTagCompound) base);
		String s = base.asString();
		if (s.equals("true")) return true;
		if (s.equals("false")) return false;
		if (s.equals("null")) return null;
		return s;
	}

	private Map<String, Object> toMap(NBTTagCompound base) {
		Map<String, Object> map = new HashMap<>();
		for (String key : base.getKeys()) {
			map.put(key, toObj(base.get(key)));
		}
		return map;
	}

}
