package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;

public class BlockListener implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getBlock().getType().equals(Material.ENDER_CHEST)) {
			// MysteryManager reward give
			e.getPlayer().getInventory().removeItem(e.getItemInHand());
		}

		e.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		e.setCancelled(true);
	}

	private void mysteryChest(BlockPlaceEvent e) {
		if (!e.getBlock().getType().equals(Material.ENDER_CHEST))
			return;

		Player p = e.getPlayer();
		GamePlayer player = GamePlayerManager.getInstance().getGamePlayer(p);

		Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("mysterychest.yml"));
		FileConfiguration data = YamlConfiguration.loadConfiguration(reader);

		double basevalue = 0;

		Random random = new Random();
		Double rDouble = random.nextDouble();

		for (String item : data.getStringList("items")) {
			String[] split = item.split(" ");
			double percent = Double.valueOf(split[0].replaceAll("%", "")) * .01;
			double max = percent + basevalue;

			if (rDouble < basevalue && rDouble >= max) {
				basevalue = max;
				continue;
			}

			if (item.contains(";")) {
				split = item.split(";");

				String[] split2 = split[0].split(" ");

				String itemstring = split2[1] + split2[2];
				ItemStack parsedItem = ItemUtil.getItem(itemstring);

				itemstring = split[1];
				ItemStack parsedItem2 = ItemUtil.getItem(itemstring);

				p.getInventory().addItem(parsedItem);
				p.getInventory().addItem(parsedItem2);

				break;
			}

			if (split[1].equalsIgnoreCase("cookies")) {
				player.setCookies(player.getCookies() + 1000);
				p.sendMessage(ChatColor.GREEN + "You got 1000 cookies!!!");
				break;
			}

			String itemstring = split[1] + split[2];
			ItemStack parsedItem = ItemUtil.getItem(itemstring);

			p.getInventory().addItem(parsedItem);
			p.sendMessage(
					ChatColor.GREEN + "You got " + parsedItem.getAmount() + parsedItem.getItemMeta().getDisplayName());
			break;
		}

	}
}
