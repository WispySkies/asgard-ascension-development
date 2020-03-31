package me.arpolix.darkelf;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StatsAndPerks implements Listener {
	
	public static void openStats(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_PURPLE + "DarkElf Statistics");
		// 0 1 2 3 4 5 6 7 8
		// x x x K M D x x x
		// new
		// 0 1 2 3 4 5 6 7 8
		// x x K M X B D x x
		
		YamlConfiguration darkelf = DarkElfCore.getDarkElf();
		
		ItemStack k = new ItemStack(Material.DIAMOND_SWORD, 1);
		ItemMeta km = k.getItemMeta();
		km.setDisplayName(ChatColor.DARK_GRAY + "Kills: " + darkelf.getInt("kills"));
		k.setItemMeta(km);
		inv.setItem(2, k);
		
		ItemStack m = new ItemStack(Material.BOOK_AND_QUILL, 1);
		ItemMeta mm = m.getItemMeta();
		mm.setDisplayName(ChatColor.DARK_PURPLE + "Members");
		List<String> lore = new ArrayList<>();
		for (String str : darkelf.getStringList("members")) {
			lore.add(ChatColor.LIGHT_PURPLE + str);
		}
		mm.setLore(lore);
		m.setItemMeta(mm);
		inv.setItem(3, m);
		
		ItemStack b = new ItemStack(Material.PAPER, 1);
		ItemMeta bm = b.getItemMeta();
		bm.setDisplayName(ChatColor.DARK_PURPLE + "Balance: " + darkelf.getInt("balance"));
		b.setItemMeta(bm);
		inv.setItem(5, b);
		
		ItemStack d = new ItemStack(Material.SKULL_ITEM, 1);
		ItemMeta dm = d.getItemMeta();
		dm.setDisplayName(ChatColor.DARK_GRAY + "Deaths: " + darkelf.getInt("deaths"));
		d.setItemMeta(dm);
		inv.setItem(6, d);
		p.openInventory(inv);
	}
	
	public static void openPerks(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_PURPLE + "DarkElf Perks");
		
		// 0 1 2 3 4 5 6 7 8
		// x x P T R W O x x
		
		YamlConfiguration darkelf = DarkElfCore.getDarkElf();
		List<String> lore = new ArrayList<>();
		
		ItemStack speed = new ItemStack(Material.SUGAR, 1);
		ItemMeta speedmeta = speed.getItemMeta();
		speedmeta.setDisplayName(ChatColor.GRAY + "Speed " + darkelf.getInt("speed"));
		int speedvalue = darkelf.getInt("speed");

		if (speedvalue == 2) {
			lore.add(ChatColor.GOLD + "Speed is maxed!");
		} else if (speedvalue == 1) {
			lore.add(ChatColor.GOLD + "Upgrade using Essence!");
			lore.add(ChatColor.GRAY + "Current Level - 1");
			lore.add(ChatColor.AQUA + "Cost - 100 Essence");
		} else {
			lore.add(ChatColor.GOLD + "Upgrade using Essence!");
			lore.add(ChatColor.AQUA + "Cost - 50 Essence");
		}
		speedmeta.setLore(lore);
		speed.setItemMeta(speedmeta);
		lore.clear();
		inv.setItem(2, speed);
		
		ItemStack str = new ItemStack(Material.ANVIL, 1);
		ItemMeta strmeta = str.getItemMeta();
		strmeta.setDisplayName(ChatColor.GRAY + "Strength " + darkelf.getInt("strength"));
		int strvalue = darkelf.getInt("strength");

		if (strvalue == 2) {
			lore.add(ChatColor.GOLD + "Strength is maxed!");
		} else if (strvalue == 1) {
			lore.add(ChatColor.GOLD + "Upgrade using Essence!");
			lore.add(ChatColor.GRAY + "Current Level - 1");
			lore.add(ChatColor.AQUA + "Cost - 100 Essence");
		} else {
			lore.add(ChatColor.GOLD + "Upgrade using Essence!");
			lore.add(ChatColor.AQUA + "Cost - 50 Essence");
		}
		strmeta.setLore(lore);
		str.setItemMeta(strmeta);
		lore.clear();
		inv.setItem(3, str);
		
		ItemStack radius = new ItemStack(Material.BARRIER, 1);
		ItemMeta rm = radius.getItemMeta();
		rm.setDisplayName(ChatColor.GRAY + "Radius " + darkelf.getInt("radius"));
		int rv = darkelf.getInt("radius");
		if (rv == 3) {
			lore.add(ChatColor.GOLD + "Radius is maxed!");
		} else if (rv == 2) {
			lore.add(ChatColor.GOLD + "Upgrade using Essence!");
			lore.add(ChatColor.GRAY + "Current Level - 2");
			lore.add(ChatColor.AQUA + "Cost - 50 Essence");
		} else {
			lore.add(ChatColor.GOLD + "Upgrade using Essence!");
			lore.add(ChatColor.AQUA + "Cost - 25 Essence");
		}
		rm.setLore(lore);
		radius.setItemMeta(rm);
		lore.clear();
		inv.setItem(4, radius);
		
		ItemStack compass = new ItemStack(Material.COMPASS, 1);
		ItemMeta commet = compass.getItemMeta();
		commet.setDisplayName(ChatColor.GRAY + "Purchase a Player Tracking Compass!");
		lore.add(ChatColor.GOLD + "With Distance");
		lore.add(ChatColor.AQUA + "Cost - 75 Essence");
		commet.setLore(lore);
		compass.setItemMeta(commet);
		lore.clear();
		inv.setItem(5, compass);
		
		ItemStack compasso = new ItemStack(Material.COMPASS, 1);
		ItemMeta cpo = compasso.getItemMeta();
		cpo.setDisplayName(ChatColor.GRAY + "Purchase a Player Tracking Compass!");
		lore.add(ChatColor.GOLD + "Without Distance");
		lore.add(ChatColor.AQUA + "Cost - 50 Essence");
		cpo.setLore(lore);
		compasso.setItemMeta(cpo);
		lore.clear();
		inv.setItem(6, compasso);
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void blockStats(InventoryClickEvent e) {
		if (e.getInventory().getName().contains("Statistics")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void inventoryClicks(InventoryClickEvent e) {
		if (e.getInventory().getName().contains("Perks")) {
			int slot = e.getSlot();
			ItemStack item = e.getInventory().getItem(slot);
			if (item == null) {
				return;
			}
			if (!item.hasItemMeta()) {
				return;
			}
			if (!item.getItemMeta().hasLore()) {
				return;
			}
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			if (lore.get(0).contains("Without Distance")) {
				String lr = lore.get(1);
				int cost = Integer.parseInt(lr.substring(9, 11));
				if (!Hook.reduceEssenceFromBalance(cost)) {
					e.setCancelled(true);
					return;
				}
				ItemStack compass = new ItemStack(Material.COMPASS, 1);
				ItemMeta cm = compass.getItemMeta();
				cm.setDisplayName(ChatColor.DARK_PURPLE + "Player Tracker");
				lore.clear();
				lore.add(ChatColor.GOLD + "Without Distance");
				cm.setLore(lore);
				compass.setItemMeta(cm);
				e.getWhoClicked().getInventory().addItem(compass);
			} else if (lore.get(0).contains("With Distance")) {
				String lr = lore.get(1);
				int cost = Integer.parseInt(lr.substring(9, 11));
				if (!Hook.reduceEssenceFromBalance(cost)) {
					e.setCancelled(true);
					return;
				}
				ItemStack compass = new ItemStack(Material.COMPASS, 1);
				ItemMeta cm = compass.getItemMeta();
				cm.setDisplayName(ChatColor.DARK_PURPLE + "Player Tracker");
				lore.clear();
				lore.add(ChatColor.GOLD + "With Distance");
				cm.setLore(lore);
				compass.setItemMeta(cm);
				e.getWhoClicked().getInventory().addItem(compass);
			} else if (lore.get(0).contains("Upgrade using Essence!")) {
				// there are 2 levels based on tiers
				String lr;
				if (lore.size() == 2) {
					lr = lore.get(1);
				} else {
					lr = lore.get(2);
				}
				int cost = Integer.parseInt(lr.substring(9, 11));
				YamlConfiguration darkelf = DarkElfCore.getDarkElf();
				if (Hook.reduceEssenceFromBalance(cost)) {
					if (item.getType() == Material.SUGAR) {
						darkelf.set("speed", darkelf.getInt("speed") + 1);
					} else if (item.getType() == Material.ANVIL) {
						darkelf.set("strength", darkelf.getInt("strength") + 1);
					} else if (item.getType() == Material.BARRIER) {
						darkelf.set("radius", darkelf.getInt("radius") + 1);
					}
					DarkElfCore.saveDarkElf();
				}
			}
			e.getWhoClicked().closeInventory();
			openPerks((Player) e.getWhoClicked());
			e.setCancelled(true);
		}
	}
}
