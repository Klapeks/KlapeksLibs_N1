package com.klapeks.libs.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.klapeks.libs.PlayerHashMap;
import com.klapeks.libs.commands.Messaging;

public class CustomInventory {
	private static final int TITLE_LENGTH = 28;
	private static final String LITTLE_LETTERS = "¿¯³²ilIt ,.|:;";
	static PlayerHashMap<CustomInventory> player$invopen = new PlayerHashMap<>();
	
	
	protected String title;
	protected int maxslots;
	protected ItemStack[] content;
//	public int symbolsToSplitLore = 25;
	/** @param title - up title
	 * @param slots - slots by Y*/
	public CustomInventory(String title, int lines) {
		this.title = title;
		if (title.equals("bungeCustomInventory") && lines==-404) {} 
		else {
			lines = lines*9;
			this.maxslots = lines;
			content = new ItemStack[maxslots];
		}
//		if (this instanceof InvBlockDatabase) {
//			InvBlockDatabase bd = (InvBlockDatabase) this;
//			if (!InvVars.id$blockdb.containsKey(bd.databaseId()))
//				InvVars.id$blockdb.put(bd.databaseId(), bd);
//		}
	}
	public void fill(int x1, int y1, int x2, int y2, ItemStack item) {
		for (int x = x1;x<=x2;x++) for (int y = y1;y<=y2;y++)  setItem(x, y, item);
	}
	public static void fill(Inventory inv, int x1, int y1, int x2, int y2, ItemStack item) {
		for (int x = x1;x<=x2;x++) for (int y = y1;y<=y2;y++)  inv.setItem(GUIs.getPos(x, y), item);
	}
	public static ItemStack[] fill(ItemStack[] content, int x1, int y1, int x2, int y2, ItemStack item) {
		for (int x = x1;x<=x2;x++) for (int y = y1;y<=y2;y++)  content[GUIs.getPos(x, y)] = item;
		return content;
	}
	public void setItem(int x, int y, ItemStack item) {
		setItem(GUIs.getPos(x, y), item);
	}
	public void setItem(int slot, ItemStack item) {
		content[slot] = item;
	}
	
	ItemStack background = null;
	public void setBackground(ItemStack item) {
		background = item;
	}

	public Inventory onOpen(Player p, Inventory inv) {return inv;};
	public void onClick(InventoryClickEvent e) {e.setCancelled(true); e.getWhoClicked().sendMessage("§4NOT INITIALIZATED");};
	public void onClose(InventoryCloseEvent e) {};
	public void onDrag(InventoryDragEvent e) {};
	
	public void openInventory(Player p) {
		if (player$invopen.contains(p)) {
			doClose(new InventoryCloseEvent(p.getOpenInventory()));
//			kmPackets.gui.fakeClose(p);
		}
		openInventory(p, null);
	}
	
	protected void openInventory(Player p, Inventory defaultInventory) {
		int maxSlots = getMaxSlots(p);
		
		Inventory inv = null;
		if (defaultInventory==null || defaultInventory.getSize() > maxSlots) {
			String t = Messaging.format(p, title);
			if (isCenterTitle || true) {
				int len = ChatColor.stripColor(t).length()*2;
				for (String s : t.split("")) if (LITTLE_LETTERS.contains(s)) len--;
				if (len < TITLE_LENGTH*2) {
					t = Messaging.repeat(" ", ((int) ((TITLE_LENGTH*2-len)*0.3625))) + t;
				}
			}
			inv = Bukkit.createInventory(null, maxSlots, "§r" + t);
		} else {
			inv = defaultInventory;
		}
		if (inv==null) return;
		int _slotAdder = 0;
		for (int slot = 0; slot < maxSlots; slot++) {
			try {
				if (content==null || content.length <= slot+_slotAdder || content[slot+_slotAdder]==null) {
					inv.setItem(slot, background);
					continue;
				}
				ItemStack item = content[slot+_slotAdder].clone();
				inv.setItem(slot, item);
			} catch (Throwable e) {
				e.printStackTrace();
				inv.setItem(slot, background);
			}
		}
		
		inv = onOpen(p, inv);
		if (inv==null) return;
		player$invopen.put(p, this);
		if (defaultInventory==null) p.openInventory(inv);
		else p.updateInventory();
		player$invopen.put(p, this);
//		if (this instanceof ViewerSaver) {
//			ViewerSaver vs = (ViewerSaver) this;
//			vs.addViewer(p);
//		}
	}
	boolean isCenterTitle = false;
	public void setCenterTitle(boolean b) {
		this.isCenterTitle = b;
	}
	public boolean isCenterTitle() {
		return isCenterTitle;
	}
	protected int getMaxSlots(Player p) {
		return maxslots >= 54 ? 54 : maxslots;
	}
	
	public static CustomInventory getByPlayer(Player p) {
		if (!player$invopen.contains(p)) return null;
		return player$invopen.get(p);
	}
	public static boolean doClick(InventoryClickEvent e) {
		if (e.getView()==null) return false;
		if (e.getRawSlot()<0) return false;
		Player p = (Player) e.getWhoClicked();
		CustomInventory einv = getByPlayer(p);
		if (einv==null) return false;
		if (e.getRawSlot()<=e.getView().getTopInventory().getSize()) {
//			if (einv instanceof Pages) {
//				Pages cpi = (Pages) einv;
//				int y = 1 + (int)(e.getRawSlot() / 9);
//				int x = e.getRawSlot() % 9 + 1;
//				int[] a = cpi.pageSection();
//				if (a[0] <= x && x <= a[2] && a[1] <= y && y <= a[3]) { 
//					cpi.onPageClick(e, cpi.getPage(p));
//					return true;
//				}
//			}
		}
		einv.onClick(e);
		return true;
	}
	public static boolean doClose(InventoryCloseEvent e) {
		if (e.getView()==null) return false;
		Player p = (Player) e.getPlayer();
		CustomInventory inve = getByPlayer(p);
		if (inve==null) return false;
//		if (inve instanceof Pages) {
//			InvVars.player$page.remove(e.getPlayer());
//		}
//		if (inve instanceof ViewerSaver) {
//			((ViewerSaver) inve).removeViewer(p);
//		}
		inve.onClose(e);
		player$invopen.remove(p);
		return true;
	}
	public static boolean doDrag(InventoryDragEvent e) {
		if (e.getView()==null) return false;
		CustomInventory inv = player$invopen.get((Player) e.getWhoClicked());
		if (inv==null) return false;
		if (e.getRawSlots().size()!=1) {
			inv.onDrag(e);
			return true;
		}
		e.getView().setCursor(e.getOldCursor());
		return doClick(new InventoryClickEvent(
					e.getView(),
					SlotType.CONTAINER, 
					e.getRawSlots().iterator().next(), 
					e.getType()==DragType.SINGLE ? ClickType.RIGHT : ClickType.LEFT,
					e.getType()==DragType.SINGLE ?InventoryAction.PLACE_SOME : InventoryAction.PLACE_ONE));
	}
	
}
