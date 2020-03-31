package me.wispy.tntfill;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class tntfillCommand implements CommandExecutor {

	Main m = Main.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!cmd.getName().equalsIgnoreCase("tntfill"))
			return false;

		if (!(sender instanceof Player)) {
			sender.sendMessage(
					ChatColor.translateAlternateColorCodes('&', m.getConfig().getString("messages.invalid_access")));
			return false;
		}

		Player pl = (Player) sender;

		if (!pl.hasPermission("tntfill.fill")) {
			pl.sendMessage(ChatColor.translateAlternateColorCodes('&',
					m.getConfig().getString("messages.fill_no_permission")));
			return false;
		}

		if (args.length == 0 || args.length > 2) {
			pl.sendMessage(
					ChatColor.translateAlternateColorCodes('&', m.getConfig().getString("messages.fill_bad_amount")));
			return false;
		}

		boolean override = false;
		if (args.length == 2 && args[1].equalsIgnoreCase("-o") && pl.hasPermission("tntfill.overridelimits")) {
			override = true;
		}

		int tnt = 0;

		try {
			tnt = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			pl.sendMessage(
					ChatColor.translateAlternateColorCodes('&', m.getConfig().getString("messages.fill_bad_amount")));
			return false;
		}

		if (tnt > 576)
			tnt = 576;

		int radius = m.getConfig().getInt("amounts.radius");

		Location l = pl.getLocation();

		int dispensers_filled = 0;
		int total_dispensers = 0;

		for (double x = l.getX() - radius; x <= l.getX() + radius; x++) {
			for (double y = l.getY() - radius; y <= l.getY() + radius; y++) {
				for (double z = l.getZ() - radius; z <= l.getZ() + radius; z++) {
					if (pl.getWorld().getBlockAt(new Location(pl.getWorld(), x, y, z))
							.getType() == Material.DISPENSER) {
						total_dispensers++;

						Dispenser dispenser = (Dispenser) pl.getWorld().getBlockAt(new Location(pl.getWorld(), x, y, z))
								.getState();

						Inventory inv = dispenser.getInventory();
						ItemStack[] inventorySlotItems = inv.getContents();

						int required_tnt = tnt;

						int non_tnt_stacks = 0;
						for (ItemStack item : inventorySlotItems) {
							try {
								if (item.getType() == Material.TNT) {
									required_tnt -= item.getAmount();
								} else {
									non_tnt_stacks++;
								}
							} catch (NullPointerException e) {
							}
						}

						if (required_tnt <= 0 || non_tnt_stacks == 9) {
							// dispenser already full
							dispensers_filled++;
							continue;
						}

						int player_tnt = 0;

						for (ItemStack playerItem : pl.getInventory().getContents()) {
							try {
								if (playerItem.getType() == Material.TNT) {
									player_tnt += playerItem.getAmount();
								}
							} catch (NullPointerException e) {
							}
						}

						if (override) {
							int override_tnt_amount = tnt;
							while (override_tnt_amount > 64) {
								dispenser.getInventory().addItem(new ItemStack(Material.TNT, 64));
								override_tnt_amount -= 64;
							}
							dispenser.getInventory().addItem(new ItemStack(Material.TNT, required_tnt));
						} else {
							if (player_tnt == 0) {
								continue;
							} else if (player_tnt > required_tnt || player_tnt == required_tnt) {
								dispensers_filled++;
							} else {
								// player_tnt <= required_tnt
								required_tnt = player_tnt;
							}
							while (required_tnt > 64) {
								dispenser.getInventory().addItem(new ItemStack(Material.TNT, 64));
								required_tnt -= 64;
								pl.getInventory().removeItem(new ItemStack(Material.TNT, 64));
							}

							dispenser.getInventory().addItem(new ItemStack(Material.TNT, required_tnt));
							pl.getInventory().removeItem(new ItemStack(Material.TNT, required_tnt));
						}
					}
				}
			}
		}

		if (total_dispensers == 0) {
			pl.sendMessage(
					ChatColor.translateAlternateColorCodes('&', m.getConfig().getString("messages.no_dispensers")));
			return true;
		}

		if (dispensers_filled == total_dispensers) {
			String out = m.getConfig().getString("messages.fill_success");
			out = out.replaceAll("%dispensers%", String.valueOf(dispensers_filled));
			out = out.replaceAll("%amount%", String.valueOf(tnt));
			pl.sendMessage(ChatColor.translateAlternateColorCodes('&', out));
			return true;
		} else if (dispensers_filled < total_dispensers) {
			// insufficient tnt
			pl.sendMessage(ChatColor.translateAlternateColorCodes('&', m.getConfig().getString("messages.fill_partial")
					.replaceAll("%dispensers%", String.valueOf(dispensers_filled))));
			return false;
		}

		return false;
	}

}
