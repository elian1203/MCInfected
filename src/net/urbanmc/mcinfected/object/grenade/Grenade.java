package net.urbanmc.mcinfected.object.grenade;

import org.bukkit.Material;
import org.bukkit.entity.Item;

public abstract class Grenade {

	private Item item;

	public Grenade(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	public abstract void activate();

	public static Grenade parseGrenade(Item item) {
		Material type = item.getItemStack().getType();

		if (type == Material.TRIPWIRE_HOOK)
			return new ThrowingKnife(item);
		else if (type == Material.SLIME_BALL)
			return new StickyGrenade(item);
		else if (type == Material.SNOW_BALL)
			return new FlashGrenade(item);
		else if (type == Material.EGG)
			return new FragGrenade(item);

		return null;
	}
}
