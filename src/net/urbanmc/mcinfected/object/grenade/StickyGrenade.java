package net.urbanmc.mcinfected.object.grenade;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class StickyGrenade extends Grenade {

	public StickyGrenade(Item item) {
		super(item);
	}

	@Override
	public void activate() {
		List<Entity> nearbyEntities = this.getItem().getNearbyEntities(3, 3 ,3);

		PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 10, 1);

		for (Entity entity : nearbyEntities) {

			if(entity instanceof Player)
				((Player) entity).addPotionEffect(slowness);


		}
	}
}
