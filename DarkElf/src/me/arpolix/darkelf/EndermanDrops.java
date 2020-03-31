package me.arpolix.darkelf;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EndermanDrops implements Listener {

	@EventHandler
	public void droploot(EntityDeathEvent e) {
		if (e.getEntity().getType() != EntityType.ENDERMAN) {
			return;
		}
		if (!e.getEntity().getWorld().getName().equalsIgnoreCase("Free")) {
			return;
		}
		e.getDrops().clear();
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "crate givekey " + e.getEntity().getKiller().getName() + "DarkElf 1");
	}
}
