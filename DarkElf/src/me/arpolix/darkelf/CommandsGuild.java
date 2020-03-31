package me.arpolix.darkelf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

public class CommandsGuild implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("darkelf")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("You must be a player to use DarkElf!");
				return false;
			}
			Player p = (Player) sender;
			if (args.length < 1) {
				Economy eco = Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
				if (eco.getBalance(p.getName()) >= 100000000) {
					eco.withdrawPlayer(p.getName(), 100000000);
					// give them the permission
					for (Player pl : p.getServer().getOnlinePlayers()) {
						pl.sendMessage(DarkElfCore.message_prefix + p.getName() + " became a DarkElf!");
					}
					return true;
				} else {
					p.sendMessage(DarkElfCore.message_prefix
							+ "You do not have enough Money, Essence, and God Tokens to become a DarkElf!");
					return false;
				}
			}

			if (args[0].equalsIgnoreCase("help")) {
				printhelp(sender);
				return true;
			}
			if (!p.hasPermission("darkelf.member") && !p.hasPermission("darkelf.admin")) {
				p.sendMessage(DarkElfCore.message_prefix + "You are not a DarkElf!");
				return false;
			}

			// love that guild shit
			// hnnnnngggggg

			// create framework
			/*
			 * .yml name - String leader - UUID.toString() members - String, String, String
			 * count - int base - ChunkX ChunkY home - X Y Z chunks - X Z, X Z, X Z... speed
			 * - int strength - int radius - int kills - int invites - String, String,
			 * String...
			 */
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("create")) {
					File folder = new File(DarkElfCore.getInstance().getDataFolder() + "/");
					for (File f : folder.listFiles()) {
						if (f.getName().replaceAll(".yml", "").equalsIgnoreCase(args[1])) {
							p.sendMessage(DarkElfCore.message_prefix + "Guild name is taken!");
							return false;
						}
					}
					File f = new File(DarkElfCore.getInstance().getDataFolder() + "/" + args[1] + ".yml");
					try {
						f.createNewFile();
					} catch (IOException e) {
					}
					YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
					yml.set("name", args[1]);
					yml.set("leader", p.getUniqueId().toString());
					List<String> members = new ArrayList<>();
					members.add(p.getUniqueId().toString());
					yml.set("members", members);
					yml.set("count", 1);
					yml.set("base", null);
					yml.set("home", null);
					yml.set("chunks", null);
					yml.set("speed", 0);
					yml.set("strength", 0);
					yml.set("radius", 1);
					yml.set("kills", 0);
					List<String> invites = new ArrayList<>();
					yml.set("invites", invites);
					try {
						yml.save(f);
					} catch (IOException e) {
					}
					p.sendMessage(DarkElfCore.message_prefix + "Created guild " + args[1] + "!");
					return true;
				}
			}
			File guildfile = getGuildFileFromPlayer(p);
			if (guildfile == null) {
				p.sendMessage(DarkElfCore.message_prefix + "You are not in a guild!");
				return false;
			}

			YamlConfiguration guildyml = YamlConfiguration.loadConfiguration(guildfile);

			// all guild features below
			// admin overrides first
			if (p.hasPermission("darkelf.admin")) {
				if (args[0].equalsIgnoreCase("disband") && args.length == 2) {
					File folder = new File(DarkElfCore.getInstance().getDataFolder() + "/");
					for (File f : folder.listFiles()) {
						if (!f.getName().replaceAll(".yml", "").equalsIgnoreCase(args[1])) {
							continue;
						}
						f.delete();
						p.sendMessage(DarkElfCore.message_prefix + "Deleted guild " + args[1] + ".");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("perks") && args.length == 2) {
					File folder = new File(DarkElfCore.getInstance().getDataFolder() + "/");
					File selected = null;
					for (File f : folder.listFiles()) {
						if (f.getName().replaceAll(".yml", "").equalsIgnoreCase(args[1])) {
							selected = f;
							break;
						}
					}
					if (selected != null) {
//						Perks.openPerks(p, selected);
						p.sendMessage(DarkElfCore.message_prefix + "Opening the perks menu!");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("base") && args.length == 3) {
					if (!p.getWorld().getName().equalsIgnoreCase("Free")) {
						p.sendMessage(DarkElfCore.message_prefix + "You are not in the free world!");
						return false;
					}

					File folder = new File(DarkElfCore.getInstance().getDataFolder() + "/");
					File selected = null;
					for (File f : folder.listFiles()) {
						if (f.getName().replaceAll(".yml", "").equalsIgnoreCase(args[1])) {
							selected = f;
							break;
						}
					}
					if (selected == null) {
						p.sendMessage(DarkElfCore.message_prefix + "Could not find that guild!");
					}
					YamlConfiguration syml = YamlConfiguration.loadConfiguration(selected);

					Chunk chunk = p.getLocation().getChunk();
					if (syml.get("base") == null) {
						syml.set("base", chunk.getX() + " " + chunk.getZ());
						try {
							syml.save(selected);
						} catch (IOException e) {
						}
						p.sendMessage(DarkElfCore.message_prefix + "Set this chunk to the guild's home!");
						return true;
					} else {
						p.sendMessage(DarkElfCore.message_prefix + "This guild already has their base set!");
						return false;
					}
				} else if (args[0].equalsIgnoreCase("sethome") && args.length == 3) {
					if (!p.getWorld().getName().equalsIgnoreCase("Free")) {
						p.sendMessage(DarkElfCore.message_prefix + "You are not in the free world!");
						return false;
					}

					File folder = new File(DarkElfCore.getInstance().getDataFolder() + "/");
					File selected = null;
					for (File f : folder.listFiles()) {
						if (f.getName().replaceAll(".yml", "").equalsIgnoreCase(args[1])) {
							selected = f;
							break;
						}
					}
					if (selected == null) {
						p.sendMessage(DarkElfCore.message_prefix + "Could not find that guild!");
					}
					YamlConfiguration syml = YamlConfiguration.loadConfiguration(selected);

					Chunk chunk = p.getLocation().getChunk();
					String cl = syml.getString("base");
					if (cl == null) {
						p.sendMessage(DarkElfCore.message_prefix + "Guild does not have a base set!");
						return false;
					}
					String[] spl = cl.split(" ");
					int x = Integer.parseInt(spl[0]);
					int z = Integer.parseInt(spl[1]);
					if (x == chunk.getX() && z == chunk.getZ()) {
						String set = "Free " + p.getLocation().getX() + " " + p.getLocation().getY() + " "
								+ p.getLocation().getZ();
						syml.set("home", set);
						try {
							syml.save(selected);
						} catch (IOException e) {
						}
						p.sendMessage(DarkElfCore.message_prefix + "Set the home location to " + set);
					} else {
						p.sendMessage(DarkElfCore.message_prefix + "You must be in the center chunk of the base!");
					}
				} else if (args[0].equalsIgnoreCase("home")) {
					File folder = new File(DarkElfCore.getInstance().getDataFolder() + "/");
					File selected = null;
					for (File f : folder.listFiles()) {
						if (f.getName().replaceAll(".yml", "").equalsIgnoreCase(args[1])) {
							selected = f;
							break;
						}
					}
					if (selected == null) {
						p.sendMessage(DarkElfCore.message_prefix + "Could not find that guild!");
					}
					YamlConfiguration syml = YamlConfiguration.loadConfiguration(selected);

					String location = syml.getString("home");
					if (location == null) {
						p.sendMessage(DarkElfCore.message_prefix + "Guild does not have a home set!");
						return false;
					}
					String[] spl = location.split(" ");
					Location loc = new Location(Bukkit.getWorld(spl[0]), Integer.parseInt(spl[1]),
							Integer.parseInt(spl[2]), Integer.parseInt(spl[3]));
					p.teleport(loc);
					p.sendMessage(DarkElfCore.message_prefix + "Teleporting you to the DarkElf World.");
					return true;
				}
				return false;
			}

			if (args.length == 1) {
				// disband, perks, base, home
				if (args[0].equalsIgnoreCase("disband")) {
					if (!guildyml.getString("leader").equalsIgnoreCase(p.getUniqueId().toString())) {
						p.sendMessage(
								DarkElfCore.message_prefix + "You must be the guild leader to disband the guild!");
						return false;
					}
					File guild = getGuildFileFromPlayer(p);
					guild.delete();
					p.sendMessage(DarkElfCore.message_prefix + "Deleted guild " + args[0] + ".");
					return true;
				} else if (args[0].equalsIgnoreCase("perks")) {
//					Perks.openPerks(p, getGuildFileFromPlayer(p));
					p.sendMessage(DarkElfCore.message_prefix + "Opening the perks menu!");
					return true;
				} else if (args[0].equalsIgnoreCase("base")) {
					if (!p.getWorld().getName().equalsIgnoreCase("Free")) {
						p.sendMessage(DarkElfCore.message_prefix + "You are not in the free world!");
						return false;
					}
					if (guildyml.getString("leader").equalsIgnoreCase(p.getUniqueId().toString())) {
						Chunk chunk = p.getLocation().getChunk();
						if (guildyml.get("base") == null) {
							guildyml.set("base", chunk.getX() + " " + chunk.getZ());
							File tosave = getGuildFileFromPlayer(p);
							try {
								guildyml.save(tosave);
							} catch (IOException e) {
							}
							p.sendMessage(DarkElfCore.message_prefix + "Set this chunk to your guild's home!");
							return true;
						} else {
							p.sendMessage(DarkElfCore.message_prefix + "You already set your base chunk!");
							return false;
						}
					} else {
						p.sendMessage(DarkElfCore.message_prefix + "You must be the leader to set the chunk base!");
						return false;
					}
				} else if (args[0].equalsIgnoreCase("sethome")) {
					if (!p.getWorld().getName().equalsIgnoreCase("Free")) {
						p.sendMessage(DarkElfCore.message_prefix + "You are not in the free world!");
						return false;
					}
					Chunk chunk = p.getLocation().getChunk();
					File f = getGuildFileFromPlayer(p);
					String cl = guildyml.getString("base");
					if (cl == null) {
						p.sendMessage(DarkElfCore.message_prefix + "You have not set a base!");
						return false;
					}
					if (!guildyml.getString("leader").equalsIgnoreCase(p.getUniqueId().toString())) {
						p.sendMessage(DarkElfCore.message_prefix + "You must be the leader to set the home!");
						return false;
					}
					String[] spl = cl.split(" ");
					int x = Integer.parseInt(spl[0]);
					int z = Integer.parseInt(spl[1]);
					if (x == chunk.getX() && z == chunk.getZ()) {
						String set = "Free " + p.getLocation().getX() + " " + p.getLocation().getY() + " "
								+ p.getLocation().getZ();
						guildyml.set("home", set);
						try {
							guildyml.save(f);
						} catch (IOException e) {
						}
						p.sendMessage(DarkElfCore.message_prefix + "Set the home location to " + set);
					} else {
						p.sendMessage(DarkElfCore.message_prefix + "You must be in the center chunk of the base!");
					}
				} else if (args[0].equalsIgnoreCase("home")) {
					String location = guildyml.getString("home");
					if (location == null) {
						p.sendMessage(DarkElfCore.message_prefix + "You have not set a home!");
						return false;
					}
					String[] spl = location.split(" ");
					Location loc = new Location(Bukkit.getWorld(spl[0]), Integer.parseInt(spl[1]),
							Integer.parseInt(spl[2]), Integer.parseInt(spl[3]));
					p.teleport(loc);
					p.sendMessage(DarkElfCore.message_prefix + "Teleporting you to the DarkElf World.");
					return true;
				} else if (args[0].equalsIgnoreCase("leave")) {
					File guild = getGuildFileFromPlayer(p);
					if (guild == null) {
						p.sendMessage(DarkElfCore.message_prefix + "You are not in a guild!");
						return false;
					}
					YamlConfiguration yml = YamlConfiguration.loadConfiguration(guild);
					if (yml.getString("leader").equalsIgnoreCase(p.getUniqueId().toString())) {
						p.sendMessage(DarkElfCore.message_prefix + "You can't leave, you're the leader!");
						return false;
					}
					List<String> members = yml.getStringList("members");
					members.remove(p.getUniqueId().toString());
					yml.set("members", members);
					try {
						yml.save(guild);
					} catch (IOException e) {
					}
					return true;
				}
				return false;
			}
			if (args.length == 2) {
				// create, invite, removeinvite, join, kick, leader
				if (args[0].equalsIgnoreCase("invite")) {
					if (!guildyml.getString("leader").equalsIgnoreCase(p.getUniqueId().toString())) {
						p.sendMessage(DarkElfCore.message_prefix
								+ "You must be the guild leader to invite someone to your guild!");
						return false;
					}
					if (guildyml.getInt("count") > 19) {
						p.sendMessage(DarkElfCore.message_prefix + "You have hit your member limit!");
						return false;
					}
					for (Player pl : p.getServer().getOnlinePlayers()) {
						if (pl.getName().equalsIgnoreCase(args[1])) {
							if (pl.hasPermission("darkelf.member")) {
								p.sendMessage(DarkElfCore.message_prefix
										+ "User provided is not a DarkElf, and therefore cannot join.");
								return false;
							}
							List<String> invites = guildyml.getStringList("invites");
							invites.add(pl.getUniqueId().toString());
							File f = getGuildFileFromPlayer(p);
							guildyml.set("invites", invites);
							try {
								guildyml.save(f);
							} catch (IOException e) {
							}
							pl.sendMessage(DarkElfCore.message_prefix + "You have been invited to join a guild by "
									+ p.getName());
							return true;
						}
					}
					return false;
				} else if (args[0].equalsIgnoreCase("removeinvite")) {
					if (!guildyml.getString("leader").equalsIgnoreCase(p.getUniqueId().toString())) {
						p.sendMessage(DarkElfCore.message_prefix
								+ "You must be the guild leader to remove someone's invite!");
						return false;
					}
					for (Player pl : p.getServer().getOnlinePlayers()) {
						if (pl.getName().equalsIgnoreCase(args[1])) {
							List<String> invites = guildyml.getStringList("invites");
							invites.remove(pl.getUniqueId().toString());
							File f = getGuildFileFromPlayer(p);
							guildyml.set("invites", invites);
							try {
								guildyml.save(f);
							} catch (IOException e) {
							}
							p.sendMessage(DarkElfCore.message_prefix + "Removed " + pl.getName() + "'s invite.");
							return true;
						}
					}
					return false;
				} else if (args[0].equalsIgnoreCase("join")) {

				} else if (args[0].equalsIgnoreCase("kick")) {
					if (!guildyml.getString("leader").equalsIgnoreCase(p.getUniqueId().toString())) {
						p.sendMessage(DarkElfCore.message_prefix + "You must be the guild leader to kick someone!");
						return false;
					}
					for (Player pl : p.getServer().getOnlinePlayers()) {
						List<String> members = guildyml.getStringList("members");
						if (!pl.getName().equalsIgnoreCase(args[1])) {
							continue;
						}
						if (!members.contains(pl.getUniqueId().toString())) {
							p.sendMessage(DarkElfCore.message_prefix + "User is not a member of your guild!");
							return false;
						}
						members.remove(pl.getUniqueId().toString());
						guildyml.set("members", members);
						File f = getGuildFileFromPlayer(p);
						try {
							guildyml.save(f);
						} catch (IOException e) {
						}
						p.sendMessage(DarkElfCore.message_prefix + "Kicked " + pl.getName() + ".");
						return true;
					}
					return false;
				} else if (args[0].equalsIgnoreCase("leader")) {
					if (!guildyml.getString("leader").equalsIgnoreCase(p.getUniqueId().toString())) {
						p.sendMessage(DarkElfCore.message_prefix
								+ "You must be the guild leader to give ownership to someone!");
						return false;
					}
					for (Player pl : p.getServer().getOnlinePlayers()) {
						List<String> members = guildyml.getStringList("members");
						if (!pl.getName().equalsIgnoreCase(args[1])) {
							continue;
						}
						if (!members.contains(pl.getUniqueId().toString())) {
							p.sendMessage(DarkElfCore.message_prefix + "User is not a member of your guild!");
							return false;
						}
						guildyml.set("leader", pl.getUniqueId().toString());
						File f = getGuildFileFromPlayer(p);
						try {
							guildyml.save(f);
						} catch (IOException e) {
						}
						p.sendMessage(DarkElfCore.message_prefix + "Made " + pl.getName() + " leader.");
						return true;
					}
					return false;
				}
			}
			if (args.length == 3) {
				// leader*
				if (args[0].equalsIgnoreCase("leader")) {
					if (!p.hasPermission("darkelf.admin")) {
						p.sendMessage(DarkElfCore.message_prefix + "You don't have permission to do that!");
						return false;
					}

					File folder = new File(DarkElfCore.getInstance().getDataFolder() + "/");
					File guild = null;
					for (File f : folder.listFiles()) {
						if (!f.getName().replaceAll(".yml", "").equalsIgnoreCase(args[1])) {
							continue;
						}
						guild = f;
					}
					YamlConfiguration yml = YamlConfiguration.loadConfiguration(guild);

					for (Player pl : p.getServer().getOnlinePlayers()) {
						List<String> members = yml.getStringList("members");
						if (!pl.getName().equalsIgnoreCase(args[1])) {
							continue;
						}
						if (!members.contains(pl.getUniqueId().toString())) {
							p.sendMessage(DarkElfCore.message_prefix + "User is not a member of that guild!");
							return false;
						}
						yml.set("leader", pl.getUniqueId().toString());
						File f = getGuildFileFromPlayer(p);
						try {
							yml.save(f);
						} catch (IOException e) {
						}
						p.sendMessage(DarkElfCore.message_prefix + "Made " + pl.getName() + " leader.");
						return true;
					}
					return false;
				}
			}
		}
		return false;
	}

	public File getGuildFileFromPlayer(Player p) {
		File folder = new File(DarkElfCore.getInstance().getDataFolder() + "/");
		for (File f : folder.listFiles()) {
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
			if (yml.getStringList("members").contains(p.getUniqueId().toString())) {
				return f;
			}
		}
		return null;
	}

	private void printhelp(CommandSender pl) {
		pl.sendMessage(ChatColor.DARK_GRAY + "DarkElf by Arpolix");
		pl.sendMessage(ChatColor.GRAY + "/darkelf -- become a darkelf " + ChatColor.GOLD
				+ "(50,000,000$, 50 Essence, 75 GodTokens)");
		pl.sendMessage(ChatColor.GRAY + "/darkelf create {name} -- create a new guild!");
		pl.sendMessage(ChatColor.GRAY + "/darkelf invite {name} -- invite a player to your guild!");
		pl.sendMessage(ChatColor.GRAY + "/darkelf removeinvite {name} -- remove a users invite!");
		pl.sendMessage(ChatColor.GRAY + "/darkelf join {name} -- join a guild!");
		pl.sendMessage(ChatColor.GRAY + "/darkelf kick {name} -- kick a user from a guild!");
		pl.sendMessage(ChatColor.GRAY + "/darkelf disband [name] -- disband your guild!");
		pl.sendMessage(ChatColor.GRAY + "/darkelf leader {name} [guild] -- make this player the guild leader!");
		pl.sendMessage(ChatColor.GRAY + "/darkelf perks [guild] -- menu to display perks!");
		pl.sendMessage(ChatColor.GRAY + "/darkelf base [guild] -- sets the chunk to be the core of your base!");
		pl.sendMessage(ChatColor.GRAY + "/darkelf home [guild] -- teleports you to the guilds waypoint!");
		pl.sendMessage(ChatColor.GRAY + "/darkelf sethome [guild] -- sets the guild waypoint!");
		pl.sendMessage(ChatColor.DARK_GRAY + "[] requires the node darkelf.admin");
	}
}
