package net.urbanmc.mcinfected.object.grenade;

import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class FlashGrenade extends Grenade {

	FlashGrenade(GamePlayer thrower, Item item) {
		super(thrower, item);
	}

	@Override
	public void activate() {
		List<Entity> nearbyEntities = this.getItem().getNearbyEntities(3, 3, 3);

		Collection<PotionEffect> effects = new ArrayList<>();

		effects.add(new PotionEffect(PotionEffectType.CONFUSION, 10, 1));
		effects.add(new PotionEffect(PotionEffectType.BLINDNESS, 10, 1));

		for (Entity entity : nearbyEntities) {
			if (entity instanceof Player) {
				Player p = (Player) entity;

				if (p == getThrower().getOnlinePlayer())
					continue;

				p.addPotionEffects(effects);
			}
		}
	}
}
