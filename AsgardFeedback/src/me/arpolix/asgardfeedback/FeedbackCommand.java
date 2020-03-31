package me.arpolix.asgardfeedback;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class FeedbackCommand implements CommandExecutor {

	Map<Player, Integer> response = new HashMap<>();
	Map<Player, String> currentPoll = new HashMap<>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!label.equalsIgnoreCase("feedback")) {
			return false;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.GREEN + "AsgardFeedack by " + ChatColor.GOLD + "Arpolix");
			sender.sendMessage(ChatColor.GREEN + "For help use " + ChatColor.GOLD + "/feedback help");
			return true;
		}

		if (args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(ChatColor.GREEN + "Asgard" + ChatColor.RED + "Feedback");
			sender.sendMessage(ChatColor.GREEN + "Fill out polls, get rewards!");
			String out = ChatColor.GREEN + "Current polls are: " + ChatColor.RED;
			for (String key : Main.getInstance().getConfig().getConfigurationSection("polls").getKeys(false)) {
				out += key + ", ";
			}
			sender.sendMessage(out.substring(0, out.length() - 2));

			sender.sendMessage(ChatColor.GREEN + "To fill out a polls use " + ChatColor.RED + "/feedback <poll>");
			sender.sendMessage(
					ChatColor.GREEN + "To view poll's results use " + ChatColor.RED + "/feedback statistics <poll>");
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to fill out polls!");
			return false;
		}

		Player p = (Player) sender;

		if (args.length == 1) {

			for (String key : Main.getInstance().getConfig().getConfigurationSection("polls").getKeys(false)) {
				if (key.equalsIgnoreCase(args[0])) {
					response.put(p, 0);
					p.sendMessage("Starting poll + " + args[0] + ".");
					startPoll(p, args[0]);
					return true;
				}
			}
			p.sendMessage(ChatColor.RED + "Could not find that poll!");
			return false;
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("statistics")) {
				// show results
			}
		}

		p.sendMessage(ChatColor.RED + "You must have missed an argument somewhere.");
		return false;
	}

	public void startPoll(Player p, String poll) {
		// get poll by type
		// get questions
		// get answers to questions
		// rainbow color format
		// ??
		// profit
		
		ChatColor[] rainbow = {ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.BLUE,
				ChatColor.LIGHT_PURPLE};
		int index = 0;
		
		for (String key : Main.getInstance().getConfig().getConfigurationSection("polls." + poll).getKeys(false)) { // questions
			p.sendMessage(ChatColor.GREEN + key);
			for (String answers : Main.getInstance().getConfig().getConfigurationSection("polls." + poll + "." + key + ".answers").getKeys(false)) {
				TextComponent tc = new TextComponent(answers);
				tc.setColor(rainbow[index]);
				tc.setClickEvent(new ClickEvent(Action.));
				
				index++;
				if (index == 6)
					index = 0;
			}
		}
	}
}
