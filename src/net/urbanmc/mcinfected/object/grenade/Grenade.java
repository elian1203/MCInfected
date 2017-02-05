package net.urbanmc.mcinfected.object.grenade;

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
}
