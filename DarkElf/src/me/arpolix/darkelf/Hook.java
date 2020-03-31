package me.arpolix.darkelf;

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

	public static boolean checkPlayer(Player p) {
		return (eco.getBalance(p.getName()) >= 100000000) && (main.getPlayerManager().getTokens(p) >= 150)
				&& (te.getTokens(p) >= 100);
	}

	public static void reduceBalance(int amount, Player p) {
		eco.withdrawPlayer(p.getName(), amount);
	}

	public static void reduceGodToken(int amount, Player p) {
		main.getPlayerManager().withdrawTokens(p, amount);
	}

	public static void reduceEssence(int amount, Player p) {
		te.removeTokens(p, amount);
	}

	public static boolean reduceEssenceFromBalance(int amount) {
		if (DarkElfCore.getDarkElf().getInt("balance") >= amount) {
			DarkElfCore.getDarkElf().set("balance", DarkElfCore.getDarkElf().getInt("balance") - amount);
			DarkElfCore.saveDarkElf();
			return true;
		} else {
			return false;
		}
	}
}
