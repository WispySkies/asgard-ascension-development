package me.arpolix.batpunch;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {

	static boolean eventIsRunning = false;
	static boolean eventStarted = false;
	static int multiplier = 10;
	static List<Player> users = new ArrayList<>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!label.equalsIgnoreCase("batpunch")) {
			return false;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage("You must use this as a player!");
			return false;
		}

		Player p = (Player) sender;

		if (!eventIsRunning) {
			if (!p.hasPermission("batpunch.admin")) {
				p.sendMessage(ChatColor.DARK_GRAY + "The BatPunch event is not running!");
				return false;
			}
		}

		if (eventStarted) {
			p.sendMessage(ChatColor.DARK_GRAY + "The BatPunch event has already started!");
			return false;
		}

		if (args.length == 0) {
			
			if (users.contains(p)) {
				p.sendMessage(ChatColor.RED + "You're already in the event, to leave use /batpunch leave");
				return false;
			}
			
			for (ItemStack item : p.getInventory().getContents()) {
				if (item != null) {
					p.sendMessage(ChatColor.RED + "Your inventory must be empty to participate in the event!");
					return false;
				}
			}
			
			String lstr = Main.getInstance().getConfig().getString("batpunch.lobby");
			String[] spl = lstr.split(" ");
			int x = Integer.parseInt(spl[1]);
			int y = Integer.parseInt(spl[2]);
			int z = Integer.parseInt(spl[3]);
			Location loc = new Location(Bukkit.getWorld(spl[0]), x, y, z);
			p.teleport(loc);
			p.sendMessage(ChatColor.DARK_GRAY + "Teleporting you to the BatPunch lobby!");
			users.add(p);
			return true;
		}

		if (args[0].equalsIgnoreCase("leave")) {
			if (!users.contains(p)) {
				p.sendMessage(ChatColor.RED + "You're not part of the gamemode!");
				return false;
			}
			users.remove(p);
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
			return true;
		}
		
		if (!p.hasPermission("batpunch.admin")) {
			p.sendMessage(ChatColor.RED + "You can't use the admin commands!");
			return false;
		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("setlobby")) {
				String loc = p.getWorld().getName() + " " + (int) p.getLocation().getX() + " "
						+ (int) p.getLocation().getY() + " " + (int) p.getLocation().getZ();
				Main.getInstance().getConfig().set("batpunch.lobby", loc);
				Main.getInstance().saveConfig();
				p.sendMessage(ChatColor.GOLD + "Set the lobby to " + p.getWorld().getName() + " "
						+ (int) p.getLocation().getX() + " " + (int) p.getLocation().getY() + " "
						+ (int) p.getLocation().getZ());
				return true;
			} else if (args[0].contentEquals("setgame")) {
				String loc = p.getWorld().getName() + " " + (int) p.getLocation().getX() + " "
						+ (int) p.getLocation().getY() + " " + (int) p.getLocation().getZ();
				Main.getInstance().getConfig().set("batpunch.game", loc);
				Main.getInstance().saveConfig();
				p.sendMessage(ChatColor.GOLD + "Set the game to " + p.getWorld().getName() + " "
						+ (int) p.getLocation().getX() + " " + (int) p.getLocation().getY() + " "
						+ (int) p.getLocation().getZ());
				return true;
			} else if (args[0].equalsIgnoreCase("start")) {
				eventStarted = true;
				p.sendMessage(ChatColor.GOLD + "Starting BatPunch!");
				String loc = Main.getInstance().getConfig().getString("batpunch.game");
				String[] spl = loc.split(" ");
				int x = Integer.parseInt(spl[1]);
				int y = Integer.parseInt(spl[2]);
				int z = Integer.parseInt(spl[3]);
				Location l = new Location(Bukkit.getWorld(spl[0]), x, y, z);
				for (Player pl : users) {
					detecthit.hits.put(pl, 0);
					pl.teleport(l);
					pl.sendMessage(ChatColor.DARK_GRAY + "Teleporting you to the BatPunch event!");
					detecthit.totalhits = users.size() * multiplier;
				}
				for (int i = 0; i < users.size() * multiplier; i++) {
					Bukkit.getWorld(spl[0]).spawnEntity(l, EntityType.BAT);
				}
				return true;
			} else if (args[0].equalsIgnoreCase("help")) {
				printHelp(p);
				return true;
			}
			p.sendMessage(ChatColor.RED + "Unknown argument.");
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("event")) {
				if (Boolean.parseBoolean(args[1])) {
					eventIsRunning = true;
					p.sendMessage(ChatColor.GOLD + "Event is now running!");
					return true;
				} else {
					eventIsRunning = false;
					p.sendMessage(ChatColor.GOLD + "Event is now not running!");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("multiplier")) {
				if (Integer.parseInt(args[1]) >= 0) {
					multiplier = Integer.parseInt(args[1]);
					p.sendMessage(ChatColor.GOLD + "Multiplier set to " + Integer.parseInt(args[1]));
					return true;
				}
			} else {
				p.sendMessage(ChatColor.RED + "Unknown argument.");
				return false;
			}
		} else {
			p.sendMessage(ChatColor.RED + "Too many/little arguments!");
			return false;
		}
		return false;
	}

	public void printHelp(Player p) {
		String pref = ChatColor.GOLD + "/batpunch ";
		p.sendMessage(ChatColor.BLACK + "BatPunch by" + ChatColor.GOLD + " Arpolix");
		p.sendMessage(pref + "setlobby - Sets the spawnpoint when users join using /batpunch");
		p.sendMessage(pref + "setgame - Sets the spawnpoint of where the game should be held.");
		p.sendMessage(pref + "event <true/false> - Allows users to teleport to the waiting area.");
		p.sendMessage(pref + "start - Starts the event and teleports users to the game.");
		p.sendMessage(pref + "multiplier # - Amount of bats for each user that joins. (default: 10)");
	}
}
