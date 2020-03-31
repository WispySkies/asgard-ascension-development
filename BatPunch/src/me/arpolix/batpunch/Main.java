package me.arpolix.batpunch;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	static Main instance;
	
	public void onEnable() {
		instance = this;
		Bukkit.getLogger().info("BatPunch by Arpolix");
		
		Bukkit.getPluginCommand("batpunch").setExecutor(new Commands());
		Bukkit.getPluginManager().registerEvents(new detecthit(), this);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();
	}
	
	public static Main getInstance() {
		return instance;
	}
}
