package me.arpolix.pexstafflist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class InvClicks implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getName().contains("Staff")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void detectGlitching(InventoryMoveItemEvent e) {
		ItemMeta meta = e.getItem().getItemMeta();
		if (!meta.hasLore()) {
			return;
		}
		if (meta.getLore().get(0).toLowerCase().contains("offline") || meta.getLore().get(0).toLowerCase().contains("online")) {
			e.getSource().remove(e.getItem());
		}
	}
}
