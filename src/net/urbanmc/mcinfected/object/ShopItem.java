package net.urbanmc.mcinfected.object;

import org.bukkit.inventory.ItemStack;

public class ShopItem {

	private int place;
	private ItemStack item;
	private ShopItemType type;

	public ShopItem(int place, ItemStack item, ShopItemType type) {
		this.place = place;
		this.item = item;
		this.type = type;
	}

	public int getPlace() {
		return place;
	}

	public ItemStack getItem() {
		return item;
	}

	public ShopItemType getType() {
		return type;
	}

	public enum ShopItemType {
		CLOSE, RANK_UP, ITEM;
	}
}
