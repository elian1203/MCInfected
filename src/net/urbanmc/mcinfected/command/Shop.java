package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.ShopManager;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.ShopItem;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class Shop extends Command {

	public Shop() {
		super("shop", "command.shop", true, Arrays.asList("s", "buy"));
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		if (args.length == 0) {
			p.getOnlinePlayer().openInventory(ShopManager.getInstance().getShop(p));
			return;
		}

		if (args.length != 2 || !args[0].equalsIgnoreCase("buy")) {
			messagePlayer(p, "/" + label + " (for gui) or /" + label + " buy [item number]");
			return;
		}

		if (!isInt(args[1])) {
			messagePlayer(p, color("&4Invalid shop item number. Number should range from 1 to 6"));
			return;
		}

		int place = Integer.parseInt(args[1]) + 1;

		if (place > 6)
			return;

		ShopItem item = ShopManager.getInstance().getShopItem(place);

		ShopManager.getInstance().manageClickedItem(p, item);
	}

	private boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}


