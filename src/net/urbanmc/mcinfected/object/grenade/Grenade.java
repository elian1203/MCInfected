package net.urbanmc.mcinfected.object.grenade;

import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Item;

public abstract class Grenade {

	private GamePlayer thrower;
	private Item item;

	public Grenade(GamePlayer thrower, Item item) {
		this.thrower = thrower;
		this.item = item;
	}

	public GamePlayer getThrower() {
		return thrower;
	}

	public Item getItem() {
		return item;
	}

	public abstract void activate();

	public static Grenade parseGrenade(GamePlayer thrower, Item item) {
		Material type = item.getItemStack().getType();

		if (type == Material.TRIPWIRE_HOOK)
			return new ThrowingKnife(thrower, item);
		else if (type == Material.SLIME_BALL)
			return new StickyGrenade(thrower, item);
		else if (type == Material.SNOW_BALL)
			return new FlashGrenade(thrower, item);
		else if (type == Material.EGG)
			return new FragGrenade(thrower, item);

		return null;
	}
}
