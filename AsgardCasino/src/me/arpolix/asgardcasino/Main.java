package me.arpolix.asgardcasino;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	static Main instance;
	static YamlConfiguration yml;
	
	public void onEnable() {
		instance = this;
		Bukkit.getLogger().info("AsgardCasino by Arpolix");
		
		Bukkit.getPluginManager().registerEvents(new Clicks(), this);
		
		File dir = new File(getDataFolder() + "/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		File fy = new File(getDataFolder() + "/" + "slots.yml");
		if (!fy.exists()) {
			try {
				fy.createNewFile();
				yml = YamlConfiguration.loadConfiguration(fy);
				List<String> characters = new ArrayList<>();
				characters.add("U");
				characters.add("L");
				characters.add("T");
				characters.add("F");
				characters.add("O");
				yml.set("characters", characters);
				List<Integer> small = new ArrayList<>();
				yml.set("small", small);
				yml.set("cost", 5000);
				small.add(0);
				small.add(5000);
				small.add(7500);
				small.add(10000);
				small.add(25000);
				List<Integer> medium = new ArrayList<>();
				yml.set("medium", medium);
				
				List<Integer> large = new ArrayList<>();
				yml.set("large", large);
				yml.save(fy);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			yml = YamlConfiguration.loadConfiguration(fy);
		}
	}
	
	public static Main getInstance() {
		return instance;
	}
}
