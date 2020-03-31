package me.arpolix.darkelf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DarkElfCore extends JavaPlugin {

	static String message_prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_PURPLE + "DarkElf" + ChatColor.DARK_GRAY
			+ "]" + ChatColor.GRAY + " ";
	static String version = "1.0b";
	static String DEP = ChatColor.DARK_PURPLE + "DarkElf" + ChatColor.GRAY;
	static DarkElfCore instance;
	static YamlConfiguration yml;
	static File def;

	public void onEnable() {
		instance = this;
		Bukkit.getLogger().info("DarkElf -- By Arpolix");
		Bukkit.getLogger().info("Goodluck on your future, 54D! <3");
		Bukkit.getLogger().info("/darkelf lock/unlock");

		File dir = new File(this.getDataFolder() + "/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		def = new File(this.getDataFolder() + "/" + "DarkElf.yml");
		if (!def.exists()) {
			try {
				def.createNewFile();
				yml = YamlConfiguration.loadConfiguration(def);
				yml.set("balance", 0);
				yml.set("kills", 0);
				yml.set("deaths", 0);
				yml.set("radius", 0);
				yml.set("speed", 0);
				yml.set("strength", 0);
				yml.set("core", "x z");
				yml.set("home", "x y z");
				List<String> members = new ArrayList<String>();
				yml.set("members", members);
				yml.set("count", 0);
				yml.set("locked", "unlocked");
				yml.save(def);
			} catch (IOException e) {
				Bukkit.getLogger().warning("DarkElf.yml could not be created! bailing...");
				Bukkit.getPluginManager().disablePlugin(this);
				return;
			}
		} else {
			yml = YamlConfiguration.loadConfiguration(def);
		}

		
		// Bukkit.getPluginCommand("darkelf").setExecutor(new CommandsGuild());
		// Bukkit.getPluginManager().registerEvents(new PerksGuild(), this);
		Bukkit.getPluginCommand("darkelf").setExecutor(new Commands());
		Bukkit.getPluginManager().registerEvents(new PlayerTracker(), this);
		Bukkit.getPluginManager().registerEvents(new StatsAndPerks(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractions(), this);
		Bukkit.getPluginManager().registerEvents(new EndermanDrops(), this);
		
		Hook.initHook();
	}
	
	public static DarkElfCore getInstance() {
		return instance;
	}

	public static YamlConfiguration getDarkElf() {
		return yml;
	}

	public static void saveDarkElf() {
		try {
			yml.save(def);
		} catch (IOException e) {
			Bukkit.getLogger().warning("YML didn't save!");
		}
	}
}
