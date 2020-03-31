package me.arpolix.batpunch;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class detecthit implements Listener {

	static Map<Player, Integer> hits = new HashMap<>();
	static int totalhits;

	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (!Commands.eventStarted) {
			return;
		}
		if (e.getEntityType() == EntityType.BAT && e.getDamager() instanceof Player) {
			if (Commands.users.contains((Player) e.getDamager())) {
				LivingEntity livEnt = (LivingEntity) e.getEntity();
				livEnt.setHealth(0);
				hits.put((Player) e.getDamager(), hits.get((Player) e.getDamager()) + 1);
				totalhits--;
				if (totalhits < 1) { // 0 or -1
					Commands.eventIsRunning = false;
					Commands.eventStarted = false;
					int highest = 0;
					Player best = null;
					for (Player p : hits.keySet()) {
						if (hits.get(p) > highest) {
							highest = hits.get(p);
							best = p;
						}
					}
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						p.sendMessage(ChatColor.GOLD + best.getName() + ChatColor.DARK_GRAY + " has won the BatPunch event with " + highest + " kills!");
					}
					for (Player p : Commands.users) {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
					}
				}
			}
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (Commands.users.contains(e.getPlayer())) {
			Commands.users.remove(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		if (Commands.users.contains(e.getPlayer())) {
			Commands.users.remove(e.getPlayer());
		}
	}

}
