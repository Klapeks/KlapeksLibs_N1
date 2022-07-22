package com.klapeks.libs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;

public class PlayerArrayList implements Iterable<Player> {
	private List<Player> players = new ArrayList<>();
	public boolean add(Player p) {
		if (contains(p)) return false;
		return players.add(p);
	}
	public boolean contains(Player p) {
		if (players.contains(p)) return true;
		for (Player pl : players) {
			if (pl.getName().equalsIgnoreCase(p.getName())) return true;
		}
		return false;
	}
	public boolean remove(Player p) {
		if (players.remove(p)) return true;
		//!is(p1,p)
		Player[] pls = (Player[]) players.stream().filter(p1->!is(p1,p)).toArray();
		players = new ArrayList<>(Arrays.asList(pls));
		return true;
	}
	
	@Override
	public Iterator<Player> iterator() {
		return players.iterator();
	}
	
	private static boolean is(Player p1, Player p2) {
		return p1.getUniqueId()==p2.getUniqueId() 
			|| p1.getName().equalsIgnoreCase(p2.getName());
	}
}
