package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.MysteryManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Material type = e.getBlock().getType();

		if (type.equals(Material.CAKE)) {
			return;
		} else if (type.equals(Material.ENDER_CHEST)) {
			GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(e.getPlayer());
			MysteryManager.getInstance().rewardPlayer(p);
			e.getPlayer().getInventory().removeItem(e.getItemInHand());
		}

		e.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		e.setCancelled(true);
	}
}
