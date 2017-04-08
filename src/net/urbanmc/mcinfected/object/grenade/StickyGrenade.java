package net.urbanmc.mcinfected.object.grenade;

import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class StickyGrenade extends Grenade {

	StickyGrenade(GamePlayer thrower, Item item) {
		super(thrower, item);
	}

	@Override
	public void activate() {
		System.out.print("Sticky Grenade Activated");
		List<Entity> nearbyEntities = this.getItem().getNearbyEntities(3, 3, 3);

		PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 200, 1);

		for (Entity entity : nearbyEntities) {
			if (entity instanceof Player)
				((Player) entity).addPotionEffect(slowness);
		}
	}
}
