package net.urbanmc.mcinfected.object;

import org.bukkit.inventory.ItemStack;

public class ShopItem {

	private int place;
	private ItemStack item;

	public ShopItem(int place, ItemStack item) {
		this.place = place;
		this.item = item;
	}

	public int getPlace() {
		return place;
	}
}
