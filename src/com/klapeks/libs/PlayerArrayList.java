package com.klapeks.libs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerArrayList implements Iterable<Player> {
	private List<UUID> players = new ArrayList<>();
	public boolean add(Player p) {
		if (contains(p)) return false;
		return players.add(p.getUniqueId());
	}
	public boolean contains(Player p) {
		return players.contains(p.getUniqueId());
	}
	public boolean remove(Player p) {
		return players.remove(p.getUniqueId());
	}
	
	@Override
	public Iterator<Player> iterator() {
		Iterator<UUID> it = players.iterator();
		return new Iterator<Player>() {
			@Override
			public Player next() {
				return Bukkit.getPlayer(it.next());
			}
			@Override
			public boolean hasNext() {
				return it.hasNext();
			}
		};
	}
	
}
