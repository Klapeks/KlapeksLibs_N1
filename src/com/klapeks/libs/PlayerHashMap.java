package com.klapeks.libs;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerHashMap<V> {
	
	private Map<UUID, V> map = new HashMap<>();

	public V put(Player p, V v) {
		return map.put(p.getUniqueId(), v);
	}
	public boolean contains(Player p) {
		return map.containsKey(p.getUniqueId());
	}
	public V remove(Player p) {
		return map.remove(p.getUniqueId());
	}
	public V get(Player p) {
		return map.get(p.getUniqueId());
	}
	
	public void forEach(BiConsumer<Player, V> c) {
		map.forEach((uuid, v) -> {
			c.accept(Bukkit.getPlayer(uuid), v);
		});
	}
}
