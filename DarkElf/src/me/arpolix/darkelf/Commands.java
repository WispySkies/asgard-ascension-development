package me.arpolix.darkelf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	
	private Map<Player, Long> cooldownSethome = new HashMap<>();
	private Map<Player, Long> cooldownHome = new HashMap<>();
	private List<Player> confirm = new ArrayList<>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!label.equalsIgnoreCase("darkelf")) {
			return false;
		}
		
		YamlConfiguration darkelf = DarkElfCore.getDarkElf();
		
		if (darkelf == null) {
			sender.sendMessage(DarkElfCore.message_prefix + "DarkElf file was null.");
			return false;
		}
		
		if (args.length > 0 && sender.hasPermission("darkelf.admin")) {
			if (args[0].equalsIgnoreCase("lock")) {
				darkelf.set("locked", "locked");
				DarkElfCore.saveDarkElf();
				sender.sendMessage(DarkElfCore.message_prefix + "Locked!");
				return true;
			} else if (args[0].equalsIgnoreCase("unlock")) {
				darkelf.set("locked", "unlocked");
				DarkElfCore.saveDarkElf();
				sender.sendMessage(DarkElfCore.message_prefix + "Unlocked!");
				return true;
			}
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to be a DarkElf!");
			return false;
		}
		Player p = (Player) sender;
		
		if (!p.hasPermission("darkelf.admin") && darkelf.getString("locked").equalsIgnoreCase("locked")) {
			if (p.hasPermission("darkelf.preorder")) {
				p.sendMessage(DarkElfCore.message_prefix + "Thanks for preordering " + DarkElfCore.DEP + "!");
			} else {
				p.sendMessage(DarkElfCore.message_prefix + DarkElfCore.DEP + " is currently locked, so you can't use it.");
				return false;
			}
		}
		
		if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
			printHelp(p);
			return false;
		}
		
		if (args[0].equalsIgnoreCase("ascend")) {
			if (p.hasPermission("darkelf.member")) {
				p.sendMessage(DarkElfCore.message_prefix + "You are already a " + DarkElfCore.DEP + "!");
				return true;
			}
			if (darkelf.getInt("count") == 20) {
				p.sendMessage(DarkElfCore.message_prefix + "There are only 20 " + ChatColor.DARK_PURPLE + "DarkElves" + ChatColor.GRAY + " allowed in the world!");
				return false;
			}
			
			if (!confirm.contains(p)) {
				confirm.add(p);
				p.sendMessage(DarkElfCore.message_prefix + "Are you sure to want to ascend? To confirm type /darkelf ascend");
				p.sendMessage(DarkElfCore.message_prefix + "To cancel type /darkelf cancel");
				return true;
			} else {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("cancel")) {
						confirm.remove(p);
						p.sendMessage(DarkElfCore.message_prefix + "Purchase cancelled!");
						return true;
					}
				}
			}
			
			if (Hook.checkPlayer(p)) {
				Hook.reduceBalance(100000000, p);
				Hook.reduceGodToken(150, p);
				Hook.reduceEssence(100, p);
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + p.getName() + " add darkelf.member");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + p.getName() + " group add DarkElf");
				for (Player pl : p.getServer().getOnlinePlayers()) {
					pl.sendMessage(DarkElfCore.message_prefix + p.getName() + " became a DarkElf!");
				}
				darkelf.set("count", darkelf.getInt("count") + 1);
				List<String> members = darkelf.getStringList("members");
				members.add(p.getName());
				darkelf.set("members", members);
				DarkElfCore.saveDarkElf();
				return true;
			} else {
				p.sendMessage(DarkElfCore.message_prefix
						+ "You do not have enough Money, Essence, and God Tokens to become a DarkElf!");
				return false;
			}
		}

		if (args[0].equalsIgnoreCase("statistics")) {
			StatsAndPerks.openStats(p);
			return true;
		}
		
		if (!p.hasPermission("darkelf.member")) {
			p.sendMessage(DarkElfCore.message_prefix + "You must be a " + DarkElfCore.DEP + " member to use " + DarkElfCore.DEP + " features!");
			p.sendMessage(DarkElfCore.message_prefix + "To ascend use /darkelf ascend, to get help use /darkelf help");
			return false;
		}
		
		// balance/deposit
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("deposit")) {
				try {
					Hook.reduceEssence(Integer.parseInt(args[1]), p);
					DarkElfCore.getDarkElf().set("balance", DarkElfCore.getDarkElf().getInt("balance") + Integer.parseInt(args[1]));
					p.sendMessage(DarkElfCore.message_prefix + "Deposited " + Integer.parseInt(args[1]) + " essence!");
					return true;
				} catch (Exception e) {
					p.sendMessage(DarkElfCore.message_prefix + "That is not a valid amount of essence!");
					return false;
				}
			}
		}
		
		if (args[0].equalsIgnoreCase("upgrade")) {
			StatsAndPerks.openPerks(p);
			return true;
		} else if (args[0].equalsIgnoreCase("core")) {
			if (!p.getWorld().getName().equalsIgnoreCase("Free")) {
				p.sendMessage(DarkElfCore.message_prefix + "You must be in the Free World to use this command!");
				return false;
			}
			if (darkelf.getString("core").equalsIgnoreCase("x z")) {
				darkelf.set("core", p.getLocation().getChunk().getX() + " " + p.getLocation().getChunk().getZ());
				p.sendMessage(DarkElfCore.message_prefix + "Core location set to " + p.getLocation().getChunk().getX() + ", " + p.getLocation().getChunk().getZ());
				DarkElfCore.saveDarkElf();
				return true;
			} else {
				p.sendMessage(DarkElfCore.message_prefix + "The home base chunk is already set! Upgrade your radius with /darkelf upgrade");
				return false;
			}
		} else if (args[0].equalsIgnoreCase("sethome")) {
			if (!p.getWorld().getName().equalsIgnoreCase("Free")) {
				p.sendMessage(DarkElfCore.message_prefix + "You must be in the Free World to use this command!");
				return false;
			}
			
			if (darkelf.getString("core").equalsIgnoreCase("x z") ) {
				p.sendMessage(DarkElfCore.message_prefix + "You have not set your core chunk yet!");
				return false;
			}
			
			String core = darkelf.getString("core");
			String[] locs = core.split(" ");
			
			int x = Integer.parseInt(locs[0]);
			int z = Integer.parseInt(locs[1]);
			
			if (x != p.getLocation().getChunk().getX() || z != p.getLocation().getChunk().getZ()) {
				p.sendMessage(DarkElfCore.message_prefix + "You must be in your core chunk to use this command!");
				return false;
			}
			
			if (cooldownSethome.containsKey(p)) {
				if (System.currentTimeMillis() - cooldownSethome.get(p) < 3000) {
					p.sendMessage(DarkElfCore.message_prefix + "Sethome is on cooldown! " + (3 - (System.currentTimeMillis() - cooldownSethome.get(p)) / 1000));
					return false;
				}
			} else {
				cooldownSethome.put(p, System.currentTimeMillis());
			}
			
			darkelf.set("home", (int) p.getLocation().getX() + " " + (int) p.getLocation().getY() + " " + (int) p.getLocation().getZ());
			DarkElfCore.saveDarkElf();
			p.sendMessage(DarkElfCore.message_prefix + "Set the home location to " + (int) p.getLocation().getX() + " " + (int) p.getLocation().getY() + " " + (int) p.getLocation().getZ());
			return true;
		} if (args[0].equalsIgnoreCase("home")) {
			if (!p.getWorld().getName().equalsIgnoreCase("Free")) {
				p.sendMessage(DarkElfCore.message_prefix + "You must be in the Free World to use this command!");
				return false;
			}
			if (darkelf.getString("home").equalsIgnoreCase("x y z")) {
				p.sendMessage(DarkElfCore.message_prefix + "The home is not set, set it by using /darkelf sethome");
				return false;
			}
			
			if (cooldownHome.containsKey(p)) {
				if (System.currentTimeMillis() - cooldownHome.get(p) < 3000) {
					p.sendMessage(DarkElfCore.message_prefix + "Home is on cooldown! " + (3 - (System.currentTimeMillis() - cooldownHome.get(p)) / 1000));
					return false;
				}
			} else {
				cooldownHome.put(p, System.currentTimeMillis());
			}
			
			p.sendMessage(DarkElfCore.message_prefix + "Teleporting you to home...");
			
			String home = darkelf.getString("home");
			String[] locs = home.split(" ");
			
			double x = Double.parseDouble(locs[0]);
			double y = Double.parseDouble(locs[1]);
			double z = Double.parseDouble(locs[2]);
			
			Location loc = new Location(Bukkit.getWorld("Free"), x, y, z);
			p.teleport(loc);
			return true;
		} else if (args[0].equalsIgnoreCase("balance")) {
			p.sendMessage(DarkElfCore.message_prefix + "You have " + DarkElfCore.getDarkElf().getInt("balance") + " essence stored.");
			return true;
		} else if (args[0].equalsIgnoreCase("leave")) {
			List<String> members = darkelf.getStringList("members");
			members.remove(p.getName());
			darkelf.set("members", members);
			darkelf.set("count", darkelf.getInt("count") - 1);
			DarkElfCore.saveDarkElf();
			p.sendMessage(DarkElfCore.message_prefix + "You left the DarkElves!");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + p.getName() + " remove darkelf.member");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + p.getName() + " group remove darkelf.member");
			return true;
		} else {
			p.sendMessage(DarkElfCore.message_prefix + "Argument not recognized! Please use the syntax in /darkelf help");
		}
		return false;
	}

	public void printHelp(Player p) {
		String sde = DarkElfCore.message_prefix + "/darkelf ";
		p.sendMessage(sde + "by Arpolix ~ ver. " + DarkElfCore.version);
		p.sendMessage(sde + "ascend - Become one of the elite. (100,000,000$, 100 Essence, 150 God Tokens)");
		p.sendMessage(sde + "statistics - View DarkElf information.");
		p.sendMessage(sde + "deposit - Add money to the bank.");
		p.sendMessage(sde + "balance - View how many essence the bank has.");
		p.sendMessage(sde + "upgrade - Upgrade " + ChatColor.DARK_PURPLE + "DarkElf" + ChatColor.GRAY + " perks");
		p.sendMessage(sde + "core - Set this chunk to be your home base.");
		p.sendMessage(sde + "sethome - Set this place to be your home waypoint. (Must be in core)");
		p.sendMessage(sde + "home - Teleport to your home base.");
		p.sendMessage(sde + "leave - Abandon the DarkElves.");
	}
	
}
