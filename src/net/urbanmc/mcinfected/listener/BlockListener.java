package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.FoodManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.MysteryManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;

		Material type = e.getBlock().getType();

		if (type.equals(Material.CAKE_BLOCK)) {
			FoodManager.getInstance().getCakes().add(e.getBlock());
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
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Action action = e.getAction();

		if (action == Action.LEFT_CLICK_BLOCK) {
			Block b = e.getClickedBlock().getRelative(e.getBlockFace());

			if (b.getType().equals(Material.FIRE)) {
				e.setCancelled(true);
			}
		} else if (action == Action.RIGHT_CLICK_BLOCK) {
			Material type = e.getClickedBlock().getType();

			if (type == Material.ITEM_FRAME) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() instanceof Painting || e.getRightClicked() instanceof ItemFrame) {
			e.setCancelled(true);
		}
	}
}
