package me.wispy.tntfill;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static Main m;
	
	public void onEnable() {
		m = this;
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		getCommand("tntfill").setExecutor(new tntfillCommand());
	}
	
	public static Main getInstance() {
		return m;
	}
}
