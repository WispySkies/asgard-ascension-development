package me.arpolix.referafriend;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Refer implements CommandExecutor {

	private final int[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	private final Character[] characters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("refer")) {
			String prefix = ChatColor.RED + "Refer-A-Friend " + ChatColor.GRAY + "> " + ChatColor.WHITE;
			if (args.length > 0) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					if (args[0].equalsIgnoreCase("generate")) {
						if (Main.getInstance().getConfig().getString("tokens." + p.getUniqueId()) != null) {
							p.sendMessage(prefix + "You already have a token generated!");
							p.sendMessage(prefix + "Your token is: " + Main.getInstance().getConfig().getString("tokens." + p.getUniqueId()));
							return false;
						}
						Set<String> keys = Main.getInstance().getConfig().getConfigurationSection("tokens").getKeys(false);
						String token = "[][][][]>>>>";
						while (!keys.contains(token)) {
							token = "";
							for (int i = 0; i < 6; i++) {
								if (Math.random() >= 0.5) {
									token += ("" + numbers[(int) (Math.random() * numbers.length)]);
								} else {
									token += ("" + characters[(int) (Math.random() * characters.length)]);
								}
							}
						}
						Main.getInstance().getConfig().set("tokens." + token + ".owner", p.getUniqueId().toString());
						List<String> redeemed = new ArrayList<>();
						Main.getInstance().getConfig().set("tokens." + token + ".users", redeemed);
						Main.getInstance().saveConfig();
						p.sendMessage(prefix + "Your token is: " + token);
						return true;
					}
					if (args.length == 2) {
						if (args[0].equalsIgnoreCase("redeem")) {
							if (p.hasPlayedBefore()) {
								p.sendMessage(prefix + "You've already played before and therefore cannot use a referral token!");
								return false;
							}
							
							String owner = Main.getInstance().getConfig().getString("tokens." + args[1] + ".owner");
							if (owner == null) {
								p.sendMessage(prefix + "Token does not exist!");
								return false;
							}
							if (!owner.equalsIgnoreCase(p.getUniqueId().toString())) {
								boolean hasReferred = false;
								for (String token : Main.getInstance().getConfig().getConfigurationSection("tokens").getKeys(false)) {
									List<String> users = Main.getInstance().getConfig().getStringList("tokens." + token + ".users");
									if (users.contains(p.getUniqueId().toString())) {
										hasReferred = true;
									}
								}
								if (hasReferred) {
									p.sendMessage(prefix + "You've already used a referral token!");
									return false;
								}
								
								List<String> users = Main.getInstance().getConfig().getStringList("tokens." + args[1] + ".users");
								users.add(p.getUniqueId().toString());
								Main.getInstance().getConfig().set("tokens." + args[1] + ".users", users);
								Main.getInstance().getConfig().set("tokens." + args[1] + "user." + p.getUniqueId().toString(), System.currentTimeMillis());
								Main.getInstance().saveConfig();
							} else {
								p.sendMessage(prefix + "You can't use your own token!");
								return false;
							}
						} else if (args[0].equalsIgnoreCase("token")) {
							String owner = Main.getInstance().getConfig().getString("tokens." + args[1] + ".owner");
							if (owner == null) {
								p.sendMessage(prefix + "Token does not exist!");
								return false;
							}
							p.sendMessage(prefix + "Token owner: " + owner + ". Token uses: " + Main.getInstance().getConfig().getStringList("tokens." + args[1] + ".users").size());
							return true;
						}
					}
					p.sendMessage(prefix + "Unknown argument. Please run" + ChatColor.GRAY + " /refer" + ChatColor.WHITE + " and try again.");
					return false;
				} else {
					sender.sendMessage(prefix + "You need to run this command as a player!");
					return false;
				}
			} else {
				sender.sendMessage(prefix + "by Arpolix");
				sender.sendMessage(prefix + "To create a token use /refer generate");
				sender.sendMessage(prefix + "To redeem a token use /refer redeem TOKEN");
				sender.sendMessage(prefix + "To view a tokens info use /refer token TOKEN");
				return true;
			}
		}
		return false;
	}
}
