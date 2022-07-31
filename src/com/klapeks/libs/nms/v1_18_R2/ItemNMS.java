package com.klapeks.libs.nms.v1_18_R2;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTNumber;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

public class ItemNMS implements com.klapeks.libs.nms.ItemNMS {

	@Override
	public Map<String, Object> getNBT(ItemStack item) {
		if (item==null) return null;
		net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);
		if (nms.t()==null) return null;
		NBTTagCompound tag = nms.t();
		return toMap(tag);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getNBT(ItemStack item, String key) {
		if (item==null) return null;
		net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);
		if (nms.t()==null) nms.c(new NBTTagCompound());
		NBTTagCompound tag = nms.t();
		if (!tag.e(key)) return null;
		return (T) toObj(tag.p(key));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getNBT(ItemStack item, String key, Class<T> clazz) {
		if (item==null) return null;
		net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);
		if (nms.t()==null) nms.c(new NBTTagCompound());
		NBTTagCompound tag = nms.t();
		if (!tag.e(key)) return null;
		if (String.class.equals(clazz)) return (T) tag.l(key);
		return (T) toObj(tag.c(key));
	}



	@Override
	public ItemStack setNBT(ItemStack item, Map<String, Object> map) {
		if (item==null) return item;
		net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = (NBTTagCompound) toBase(map);
		nms.c(tag);
		return CraftItemStack.asBukkitCopy(nms);
	}

	@Override
	public ItemStack addNBT(ItemStack item, Map<String, Object> map) {
		if (item==null) return item;
		net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);
		if (nms.t()==null) nms.c(new NBTTagCompound());
		NBTTagCompound tag = nms.t();
		for (Object key : map.keySet()) {
			tag.a(key.toString(), toBase(map.get(key)));
		}
		nms.c(tag);
		return CraftItemStack.asBukkitCopy(nms);
	}
	@Override
	public ItemStack setNBT(ItemStack item, String key, Object value) {
		if (item==null) return item;
		net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);
		if (nms.t()==null) nms.c(new NBTTagCompound());
		NBTTagCompound tag = nms.t();
		tag.a(key, toBase(value));
		nms.c(tag);
		return CraftItemStack.asBukkitCopy(nms);
	}
	
	@Override
	public boolean hasNBT(ItemStack item, String key) {
		if (item==null) return false;
		net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);
		if (nms.t()==null) return false;
		return nms.t().e(key);
	}

	
	private NBTBase toBase(Object object) {
		if (object==null) return null;
		if (object instanceof NBTBase) return (NBTBase) object;
		if (object instanceof Map<?,?>) {
			Map<?,?> map = (Map<?,?>) object;
			NBTTagCompound tag = new NBTTagCompound();
			for (Object key : map.keySet()) {
				tag.a(key.toString(), toBase(map.get(key)));
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
		if (base instanceof NBTTagIntArray) return ((NBTTagIntArray) base).f();
		if (base instanceof NBTTagCompound) return toMap((NBTTagCompound) base);
		String s = base.e_();
		if (s.equals("true")) return true;
		if (s.equals("false")) return false;
		if (s.equals("null")) return null;
		return s;
	}

	private Map<String, Object> toMap(NBTTagCompound base) {
		Map<String, Object> map = new HashMap<>();
		for (String key : base.d()) {
			map.put(key, toObj(base.c(key)));
		}
		return map;
	}

}
