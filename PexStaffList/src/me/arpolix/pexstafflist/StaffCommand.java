package me.arpolix.pexstafflist;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.kitteh.vanish.VanishManager;
import org.kitteh.vanish.VanishPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class StaffCommand implements CommandExecutor {

	@SuppressWarnings("unused")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!label.equalsIgnoreCase("staff")) {
			return false;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage("You need to be a player to run this command!");
			return false;
		}
		
		sender.sendMessage(ChatColor.GREEN + "Loading the staff!");
		
		PermissionManager pex = PermissionsEx.getPermissionManager();
		int total = 0;
		for (String group : Main.yml.getStringList("ranks")) {
			for (PermissionUser user : pex.getGroup(group).getUsers()) {
				total++;
			}
		}
		
		while (total % 9 != 0)
			total++;
		
		Inventory inv = Bukkit.createInventory(null, total, ChatColor.DARK_RED + Bukkit.getServerName() + " Staff");
		
		List<PermissionUser> added = new ArrayList<>();
		boolean duplicate = false;
		for (String group : Main.yml.getStringList("ranks")) {
			for (PermissionUser user : pex.getGroup(group).getUsers()) {
				for (PermissionUser pu : added) {
					if (pu == user)
						duplicate = true;
				}
				if (duplicate) {
					duplicate = false;
					continue;
				}
				added.add(user);
				ItemStack item = new ItemStack(Material.SKULL_ITEM, 1);
				SkullMeta meta = (SkullMeta) item.getItemMeta();
				meta.setOwner(user.getName());
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', pex.getGroup(group).getSuffix()) + ChatColor.GRAY + " " + user.getName());
				List<String> lore = new ArrayList<>();
				VanishManager vm = new VanishManager((VanishPlugin) sender.getServer().getPluginManager().getPlugin("VanishNoPacket"));
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (p.getName().equalsIgnoreCase(user.getName())) {
						if (!vm.isVanished(p)) {
							lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "ONLINE");
							break;
						}
					}
				}
				if (lore.size() == 0) {
					lore.add(ChatColor.RED + "" + ChatColor.BOLD + "OFFLINE");
				}
		        
				meta.setLore(lore);
				item.setItemMeta(meta);
				inv.addItem(item);
			}
		}
		((Player) sender).openInventory(inv);
		return true;
	}
}
