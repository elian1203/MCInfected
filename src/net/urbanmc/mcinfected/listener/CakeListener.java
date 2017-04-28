package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.FoodManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Cake;

public class CakeListener implements Listener {

	@EventHandler
	public void onCakeEat(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		Block b = e.getClickedBlock();

		if (!(b.getState().getData() instanceof Cake))
			return;

		Cake cake = (Cake) b.getState().getData();

		Player player = e.getPlayer();

		double health = player.getHealth();

		if (health == 20)
			return;

		health += 8;

		if (health > 20) {
			health = 20;
		}

		player.setHealth(health);

		int slicesRemaining = cake.getSlicesRemaining();

		cake.setSlicesRemaining(slicesRemaining - 1);

		if (cake.getSlicesRemaining() == 0) {
			b.setType(Material.AIR);
			FoodManager.getInstance().getCakes().remove(b);
		} else {
			b.setData(cake.getData());
		}

		e.setCancelled(true);
	}
}
