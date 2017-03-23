package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.ShopManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ShopListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		Inventory shop = e.getClickedInventory();

		if (shop == null)
			return;

		if (!shop.getTitle().equals("Shop"))
			return;

		GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(p);
		ShopItem item = ShopManager.getInstance().getShopItem(e.getSlot());

		ShopManager.getInstance().manageClickedItem(gamePlayer, item);

		e.setCancelled(true);
	}
}
