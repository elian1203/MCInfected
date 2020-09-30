package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.FoodManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Cake;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CakeListener implements Listener {

	@EventHandler
	public void onCakeEat(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		Block b = e.getClickedBlock();

		if (b == null)
			return;

		if (!(b.getBlockData() instanceof Cake))
			return;

		Cake cake = (Cake) b.getBlockData();

		Player player = e.getPlayer();

		double health = player.getHealth();

		if (health == 20)
			return;

		health += 8;

		if (health > 20) {
			health = 20;
		}

		player.setHealth(health);

		cake.setBites(cake.getBites() + 1);

		if (cake.getBites() == cake.getMaximumBites()) {
			b.setType(Material.AIR);
			FoodManager.getInstance().getCakes().remove(b);
		} else {
			b.setBlockData(cake);
		}

		e.setCancelled(true);
	}
}
