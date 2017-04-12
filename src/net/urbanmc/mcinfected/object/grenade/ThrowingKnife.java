package net.urbanmc.mcinfected.object.grenade;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.List;

public class ThrowingKnife extends Grenade {

	ThrowingKnife(GamePlayer thrower, Item item) {
		super(thrower, item, GrenadeType.THROWING_KNIFE);
	}

	@Override
	public void activate() {
		//Activate is never actually ran
		List<Entity> nearbyEntities = this.getItem().getNearbyEntities(1, 2, 1);

		for (Entity entity : nearbyEntities) {

			if (entity instanceof Player) {
				Player player = (Player) entity;


				GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);
				p.setLastAttacker(getThrower());
			}
		}
	}
}
