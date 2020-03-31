package me.arpolix.asgardfeedback;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.nekrosius.asgardascension.Main;
import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;

import net.milkbowl.vault.economy.Economy;

@SuppressWarnings("deprecation")
public class Hook {

	static Economy eco;
	static Main main;
	static TokenEnchantAPI te;
	
	public static void initHook() {
		eco = Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
		main = (Main) Bukkit.getPluginManager().getPlugin("AsgardAscension");
		if (main == null) {
			Bukkit.getLogger().warning("AsgardAscension was null");
		}
		te = (TokenEnchantAPI) Bukkit.getPluginManager().getPlugin("TokenEnchant");
	}
	
	public static boolean reduceBalance(int amount, Player p) {
		if (eco.getBalance(p.getName()) >= amount) {
			eco.withdrawPlayer(p.getName(), amount);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean reduceGodToken(int amount, Player p) {
		if (main.getPlayerManager().getTokens(p) >= amount) {
			main.getPlayerManager().withdrawTokens(p, amount);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean reduceEssence(int amount, Player p) {
		if (te.getTokens(p) >= amount) {
			te.removeTokens(p, amount);
			return true;
		} else {
			return false;
		}
	}
}
