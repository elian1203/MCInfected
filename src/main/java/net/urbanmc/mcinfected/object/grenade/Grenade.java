package net.urbanmc.mcinfected.object.grenade;

import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.util.PacketUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Grenade {

	private GamePlayer thrower;
	private Item item;

	Grenade(GamePlayer thrower, Item item) {
		this.thrower = thrower;
		this.item = item;
	}

	public static boolean isGrenade(ItemStack is) {
		Material type = is.getType();

		return type.equals(Material.TRIPWIRE_HOOK) || type.equals(Material.SLIME_BALL) ||
				type.equals(Material.SNOW_BALL) || type.equals(Material.EGG);
	}

	public static Grenade parseGrenade(GamePlayer thrower, Item item) {
		Material type = item.getItemStack().getType();

		switch (type) {
			case TRIPWIRE_HOOK:
				return new ThrowingKnife(thrower, item);
			case SLIME_BALL:
				return new StickyGrenade(thrower, item);
			case SNOW_BALL:
				return new FlashGrenade(thrower, item);
			case EGG:
				return new FragGrenade(thrower, item);
			default:
				return null;
		}
	}

	GamePlayer getThrower() {
		return thrower;
	}

	public Item getItem() {
		return item;
	}

	public abstract void activate();

	void createHelix(List<Entity> nearbyEntities) {
		Location loc = getItem().getLocation();

		int radius = 2;

		for (double y = 0; y <= 4; y += 0.05) {
			double x = radius * Math.cos(y);
			double z = radius * Math.sin(y);

			PacketUtil.sendExplosionParticles(nearbyEntities,
			                                  (float) (x + loc.getX()),
			                                  (float) (y + loc.getY()),
			                                  (float) (z + loc.getZ()));
		}
	}
}
