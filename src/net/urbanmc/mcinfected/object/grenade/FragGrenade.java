package net.urbanmc.mcinfected.object.grenade;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.List;

public class FragGrenade extends Grenade {

	FragGrenade(GamePlayer thrower, Item item) {
		super(thrower, item);
	}

	@Override
	public void activate() {
		List<Entity> nearbyEntities = this.getItem().getNearbyEntities(3, 3, 3);

		for (Entity entity : nearbyEntities) {
			if (entity instanceof Player) {
				Player player = (Player) entity;

				player.damage(10);

				GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);
				p.setLastAttacker(getThrower());
			}
		}
	}
}
