package me.arpolix.darkelf;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerInteractions implements Listener {


	@EventHandler
	public void pltp(PlayerTeleportEvent e) {
		if (!e.getPlayer().hasPermission("darkelf.member")) {
			return;
		}
		if (!e.getTo().getWorld().getName().equalsIgnoreCase("Free") && e.getFrom().getWorld().getName().equalsIgnoreCase("Free")) {
			e.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
		}
		if (e.getTo().getWorld().getName().equalsIgnoreCase("Free")) {
			YamlConfiguration darkelf = DarkElfCore.getDarkElf();
			if (darkelf.getInt("strength") != 0) {
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, darkelf.getInt("strength") - 1));
			}
			if (darkelf.getInt("strength") != 0) {
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE,  darkelf.getInt("speed") - 1));
			}
		}
	}

	@EventHandler
	public void death(PlayerDeathEvent e) {
		if (e.getEntity().hasPermission("darkelf.member")) {
			DarkElfCore.getDarkElf().set("deaths", DarkElfCore.getDarkElf().getInt("deaths") + 1);
			DarkElfCore.saveDarkElf();
		}
	}

	@EventHandler
	public void kill(PlayerDeathEvent e) {
		try {
			if (e.getEntity().getKiller() == null) {
				return;
			}
		} catch (NullPointerException e1) {
			return;
		}
		if (e.getEntity().getKiller().hasPermission("darkelf.member")) {
			DarkElfCore.getDarkElf().set("kills", DarkElfCore.getDarkElf().getInt("kills") + 1);
			DarkElfCore.saveDarkElf();
		}
	}
}
