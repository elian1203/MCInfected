package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.FoodManager;
import net.urbanmc.mcinfected.object.Food;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FoodConsumeListener implements Listener {

	@EventHandler
	public void onFoodConsume(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		ItemStack is = e.getItem();

		if (is == null)
			return;

		Food food = FoodManager.getInstance().getFoodByMaterial(is.getType());

		if (food == null)
			return;

		Player player = e.getPlayer();

		double health = player.getHealth();

		health += food.getHealthReplenished();

		if (health > 20) {
			health = 20;
		}

		player.setHealth(health);
		player.getInventory().removeItem(new ItemStack(is.getType()));
	}
}
