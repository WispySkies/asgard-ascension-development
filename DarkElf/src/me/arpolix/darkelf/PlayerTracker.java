package me.arpolix.darkelf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerTracker implements Listener {

	private Map<Player, Long> cooldown = new HashMap<>();
	
	@EventHandler
	public void rightClick(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR) {
			if (e.getItem() != null) {
				if (e.getItem().getType() == Material.COMPASS) {
					ItemStack item = e.getItem();
					if (!item.hasItemMeta()) {
						return;
					}
					ItemMeta meta = item.getItemMeta();
					if (!meta.hasLore()) {
						return;
					}
					List<String> lore = meta.getLore();
					
					if (!lore.get(0).contains("Distance")) {
						return;
					}
					
					Player p = e.getPlayer();
					
					if (!cooldown.containsKey(p)) {
						cooldown.put(p, System.currentTimeMillis());
					} else {
						if (System.currentTimeMillis() - cooldown.get(p) >= 5000) {
							cooldown.remove(p);
						} else {
							long seconds = (System.currentTimeMillis() - cooldown.get(p));
							p.sendMessage(DarkElfCore.message_prefix + "The compass is on a cooldown! " + (5 - (seconds / 1000)) + " seconds left!");
							return;
						}
					}
					int sd = Integer.MAX_VALUE;
					Player target = null;
					for (Player pl : p.getWorld().getPlayers()) {
						if (pl.hasPermission("darkelf.member")) {
							continue;
						}
						if (p.getLocation().distance(pl.getLocation()) < sd) {
							sd = (int) p.getLocation().distance(pl.getLocation());
							target = pl;
						}
					}
					if (target == null || sd == Integer.MAX_VALUE) {
						p.sendMessage(DarkElfCore.message_prefix + "There are no survivors in the Free World!");
						return;
					}
					if (lore.get(0).contains("Without Distance")) {
						p.sendMessage(DarkElfCore.message_prefix + "Set your compass towards " + target.getName());
						p.setCompassTarget(target.getLocation());
					} else if (lore.get(0).contains("With Distance")) {
						p.sendMessage(DarkElfCore.message_prefix + "Set your compass towards " + target.getName() + ". He is " + sd + " blocks away.");
						p.setCompassTarget(target.getLocation());
					}
					return;
				}
			}
		}
	}
}
