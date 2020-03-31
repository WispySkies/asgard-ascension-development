package me.arpolix.referafriend;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static Main instance;
	
	public void onEnable() {
		Bukkit.getLogger().info("ReferAFriend by Arpolix");
		instance = this;
		Bukkit.getServer().getPluginCommand("refer").setExecutor(new Refer());
		
		getConfig();
		getConfig().options().copyDefaults(true);
	}
	
	public static Main getInstance() {
		return instance;
	}
}
