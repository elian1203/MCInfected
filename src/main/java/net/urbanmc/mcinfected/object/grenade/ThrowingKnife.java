package net.urbanmc.mcinfected.object.grenade;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.util.PacketUtil;
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
		List<Entity> nearbyEntities = this.getItem().getNearbyEntities(1, 2, 1);

		for (Entity entity : nearbyEntities) {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

				if (!p.isInfected())
					continue;

				p.setLastAttacker(getThrower());

				double damage = (getItem().getLocation().getY() - player.getLocation().getY()) > 1.43D ? 20 : 16;

				player.damage(damage);

				PacketUtil.sendActionBar(player, "§4You have been hit with a throwing knife!", "red");
			}
		}
	}
}
