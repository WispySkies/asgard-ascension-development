package me.arpolix.pexstafflist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	static Main instance;
	private static File f;
	static YamlConfiguration yml;

	public void onEnable() {

		instance = this;
		Bukkit.getLogger().info("PexStaffList by Arpolix");
		Bukkit.getPluginManager().registerEvents(new InvClicks(), this);
		Bukkit.getPluginCommand("staff").setExecutor(new StaffCommand());
		File dir = new File(getDataFolder() + "/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		f = new File(getDataFolder() + "/" + "ranks.yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e1) {
				Bukkit.getLogger().warning("Couldn't create file!");
			}
			yml = YamlConfiguration.loadConfiguration(f);
			List<String> ranks = new ArrayList<>();
			ranks.add("Owner");
			ranks.add("Manager");
			ranks.add("Administrator");
			ranks.add("SrMod");
			ranks.add("Moderator");
			ranks.add("Helper");
			yml.set("ranks", ranks);
			try {
				yml.save(f);
			} catch (IOException e) {
				Bukkit.getLogger().warning("File didn't save!");
			}
		} else {
			yml = YamlConfiguration.loadConfiguration(f);
		}
	}

	public static void saveYML() {
		try {
			yml.save(f);
		} catch (IOException e) {
		}
	}

	public static Main getInstance() {
		return instance;
	}
}
