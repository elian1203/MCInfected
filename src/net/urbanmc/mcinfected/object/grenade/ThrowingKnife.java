package net.urbanmc.mcinfected.object.grenade;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.List;

public class ThrowingKnife extends Grenade {

	ThrowingKnife(GamePlayer thrower, Item item) {
		super(thrower, item);
	}

	@Override
	public void activate() {
		Location loc = getItem().getLocation();
		List<Entity> nearbyEntities = this.getItem().getNearbyEntities(1, 2, 1);

		for (Entity entity : nearbyEntities) {

			if (entity instanceof Player) {
				Player player = (Player) entity;

				player.damage(16);

				GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);
				p.setLastAttacker(getThrower());
			}
		}
	}
}
