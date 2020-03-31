package me.arpolix.asgardfeedback;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	static Main instance;
	
	public void onEnable() {
		
		Bukkit.getPluginCommand("feedback").setExecutor(new FeedbackCommand());
		
		Bukkit.getLogger().info("AsgardFeedback - By Arpolix");
		Bukkit.getLogger().info("/feedback");
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();
		
		Hook.initHook();
	}
	
	public static Main getInstance() {
		return instance;
	}
}
