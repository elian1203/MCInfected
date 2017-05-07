package net.urbanmc.mcinfected.object.grenade;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.util.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class ThrowingKnife extends Grenade {

	ThrowingKnife(GamePlayer thrower, Item item) {
		super(thrower, item);
	}

	@SuppressWarnings("deprecation")
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

				double health = player.getHealth(), damage =
						(getItem().getLocation().getY() - player.getLocation().getY()) > 1.43D ? 20 : 16;

				health -= damage;

				if (health <= 0) {
					EntityDamageEvent e = new EntityDamageEvent(player, DamageCause.ENTITY_ATTACK, damage);
					Bukkit.getPluginManager().callEvent(e);
				} else {
					player.setHealth(health);
				}

				PacketUtil.sendActionBar(player, "You have been hit with a throwing knife!", "dark_red");
			}
		}
	}
}
