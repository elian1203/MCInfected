package net.urbanmc.mcinfected.listener;

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

		health += 4;

		if (health > 20) {
			health = 20;
		}

		player.setHealth(health);

		int slicesEaten = cake.getSlicesEaten();

		cake.setSlicesEaten(slicesEaten + 1);
		cake.setSlicesRemaining(slicesEaten - 1);

		b.getState().setData(cake);
	}
}
