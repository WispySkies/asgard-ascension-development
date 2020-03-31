package me.arpolix.referafriend;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GiveReward implements Listener {

	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		for (String token : Main.getInstance().getConfig().getConfigurationSection("tokens").getKeys(false)) {
			for (String uuid : Main.getInstance().getConfig().getConfigurationSection("tokens." + token + "user").getKeys(false)) {
				if (!uuid.equalsIgnoreCase(p.getUniqueId().toString()))
					continue;
				if (System.currentTimeMillis() - Main.getInstance().getConfig().getLong("tokens." + token + ".user." + p.getUniqueId().toString()) >= 3600000) {
					Main.getInstance().getConfig().set("tokens." + token + ".user." + p.getUniqueId().toString(), null);
					
				}
			}
		}
	}
}
