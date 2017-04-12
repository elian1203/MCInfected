package net.urbanmc.mcinfected.object.grenade;


import net.minecraft.server.v1_11_R1.EnumParticle;
import net.minecraft.server.v1_11_R1.PacketPlayOutWorldParticles;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class StickyGrenade extends Grenade {

	StickyGrenade(GamePlayer thrower, Item item) {
		super(thrower, item, GrenadeType.STICKY);
	}

	@Override
	public void activate() {
		System.out.print("Sticky Grenade Activated");
		List<Entity> nearbyEntities = this.getItem().getNearbyEntities(3, 3, 3);

		PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 200, 1);
		createHelix(nearbyEntities);
		for (Entity entity : nearbyEntities) {
			if (entity instanceof Player) {
				((Player) entity).addPotionEffect(slowness);
			}
		}
	}

}
