package me.arpolix.asgardcasino;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Clicks implements Listener {

	@EventHandler
	public void rightClickSign(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
	}
	
	@EventHandler
	public void leftClickSign(PlayerInteractEvent e) {
		if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
			return;
		}
		if (e.getClickedBlock().getType() != Material.SIGN) {
			return;
		}
		
		if (e.getPlayer().hasPermission("acasino.admin") && e.getPlayer().isSneaking()) {
			
		}
	}
}
