package net.urbanmc.mcinfected.object.grenade;

import net.minecraft.server.v1_11_R1.EnumParticle;
import net.minecraft.server.v1_11_R1.PacketPlayOutWorldParticles;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Grenade {

	private GamePlayer thrower;
	private Item item;
	private GrenadeType type;

	Grenade(GamePlayer thrower, Item item, GrenadeType type) {
		this.thrower = thrower;
		this.item = item;
		this.type = type;
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
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
					EnumParticle.EXPLOSION_NORMAL,
					true,
					(float) (loc.getX() + x),
					(float) (loc.getY() + y),
					(float) (loc.getZ() + z),
					0,
					0,
					0,
					0,
					1);

			nearbyEntities.forEach(entity -> {
				if (entity instanceof Player) {
					((CraftPlayer) ((Player) entity)).getHandle().playerConnection.sendPacket(packet);
				}
			});
		}
	}

	public GrenadeType getType() {
		return type;
	}

	public enum GrenadeType {
		THROWING_KNIFE, FRAG, STICKY, FLASH
	}
}
