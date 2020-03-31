package me.arpolix.darkelf;

import java.io.File;
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

@SuppressWarnings("deprecation")
public class PerksGuild implements Listener {

	public static void openPerks(Player p, File guild) {
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(guild);
		String name = yml.getString("name");
		Inventory inv = Bukkit.createInventory(null, 9,
				ChatColor.DARK_PURPLE + "DarkElf Guild: " + yml.getString("name"));
		// perks
		// upgrade the speed 1
		// upgrade strength 2
		// upgrade radius 3
		// members 4
		// base location 5
		// kills 6
		// guild info - 7
		// 0 1 2 3 4 5 6 7 8
		// x 1 2 3 7 4 5 6 x

		ItemStack usp = new ItemStack(Material.RABBIT_FOOT, 1);
		ItemStack ust = new ItemStack(Material.ANVIL, 1);
		ItemStack ur = new ItemStack(Material.BARRIER, 1);
		ItemStack info = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemStack mem = new ItemStack(Material.BOOK_AND_QUILL, 1);
		ItemStack loc = new ItemStack(Material.EMPTY_MAP, 1);
		ItemStack kills = new ItemStack(Material.DIAMOND_SWORD, 1);
		ItemMeta uspm = usp.getItemMeta();
		ItemMeta ustm = ust.getItemMeta();
		ItemMeta urm = ur.getItemMeta();
		ItemMeta infom = info.getItemMeta();
		ItemMeta memm = mem.getItemMeta();
		ItemMeta locm = loc.getItemMeta();
		ItemMeta killsm = kills.getItemMeta();

		List<String> lore = new ArrayList<>();

		int speed = yml.getInt("speed");
		int strength = yml.getInt("strength");
		int radius = yml.getInt("radius");

		uspm.setDisplayName(ChatColor.DARK_PURPLE + "Speed " + speed);
		ustm.setDisplayName(ChatColor.DARK_PURPLE + "Strength " + strength);
		urm.setDisplayName(ChatColor.DARK_PURPLE + "Chunk Radius " + radius);

		if (speed == 3) {
			lore.add(ChatColor.GOLD + "Maximum level!");
		} else {
			lore.add(ChatColor.GRAY + "Level: " + speed);
			lore.add(ChatColor.GRAY + "Next Level: " + (speed + 1));
			lore.add(ChatColor.GRAY + "Cost: " + (speed * 50));
		}
		uspm.setLore(lore);
		usp.setItemMeta(uspm);
		inv.setItem(1, usp);
		lore.clear();
		if (strength == 3) {
			lore.add(ChatColor.GOLD + "Maximum level!");
		} else {
			lore.add(ChatColor.GRAY + "Level: " + strength);
			lore.add(ChatColor.GRAY + "Next Level: " + (strength + 1));
			lore.add(ChatColor.GRAY + "Cost: " + (strength * 50));
		}
		ustm.setLore(lore);
		ust.setItemMeta(ustm);
		inv.setItem(2, ust);
		lore.clear();
		if (radius == 3) {
			lore.add(ChatColor.GOLD + "Maximum level!");
		} else {
			lore.add(ChatColor.GRAY + "Level: " + radius);
			lore.add(ChatColor.GRAY + "Next Level: " + (radius + 1));
			lore.add(ChatColor.GRAY + "Cost: " + (radius * 25));
		}
		urm.setLore(lore);
		ur.setItemMeta(urm);
		inv.setItem(3, ur);
		lore.clear();

		infom.setDisplayName(ChatColor.DARK_PURPLE + name);
		lore.add(ChatColor.GRAY + "Owner: " + Bukkit.getOfflinePlayer(yml.getString("leader")).getName());
		infom.setLore(lore);
		info.setItemMeta(infom);
		lore.clear();
		inv.setItem(4, info);
		memm.setDisplayName(ChatColor.DARK_PURPLE + "Members");
		for (String str : yml.getStringList("members")) {
			lore.add(ChatColor.GRAY + Bukkit.getOfflinePlayer(str).getName());
		}
		memm.setLore(lore);
		mem.setItemMeta(memm);
		lore.clear();
		inv.setItem(5, mem);
		locm.setDisplayName(ChatColor.DARK_PURPLE + "Base Location (Chunk)");
		if (yml.getString("base") == null) {
			lore.add(ChatColor.GRAY + "You don't have a base yet!");
		} else {
			String[] spl = yml.getString("base").split(" ");
			lore.add(ChatColor.GRAY + "X " + spl[0] + " Y" + spl[1]);
		}
		locm.setLore(lore);
		loc.setItemMeta(locm);
		lore.clear();
		inv.setItem(6, loc);
		killsm.setDisplayName(ChatColor.DARK_PURPLE + "Kills");
		lore.add(ChatColor.GRAY + "Total: " + yml.getInt("kills"));
		killsm.setLore(lore);
		kills.setItemMeta(killsm);
		lore.clear();
		inv.setItem(7, kills);
		p.openInventory(inv);
	}

	@EventHandler
	public void killall(InventoryClickEvent e) {
		if (e.getInventory().getName().contains(ChatColor.DARK_PURPLE + "DarkElf Guild: ")) {
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			if (item != null) {
				if (item.hasItemMeta()) {
					ItemMeta meta = item.getItemMeta();
					if (meta.hasLore()) {
						List<String> lore = meta.getLore();
						if (lore.size() == 3) {
							// take the essence
							File folder = new File(DarkElfCore.getInstance().getDataFolder() + "/");
							for (File f : folder.listFiles()) {
								String cut = e.getInventory().getName().substring(17);
								if (f.getName().equalsIgnoreCase(cut)) {
									YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
									String type = item.getItemMeta().getDisplayName().split(" ")[0];
									yml.set(type, yml.getInt(type) + 1);
									e.getWhoClicked().sendMessage(
											DarkElfCore.message_prefix + "Purchased the " + cut + "upgrade.");
									// take essence by level (lore at index 2)
								}
							}
						}
					}
				}
			}
		}
		return;
	}
}
