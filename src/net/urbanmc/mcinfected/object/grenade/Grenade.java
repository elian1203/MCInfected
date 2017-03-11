package net.urbanmc.mcinfected.object.grenade;

import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public abstract class Grenade {

	private GamePlayer thrower;
	private Item item;

	public Grenade(GamePlayer thrower, Item item) {
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

	public GamePlayer getThrower() {
		return thrower;
	}

	public Item getItem() {
		return item;
	}

	public abstract void activate();
}
