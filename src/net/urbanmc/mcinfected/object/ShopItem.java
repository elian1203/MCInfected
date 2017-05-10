package net.urbanmc.mcinfected.object;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class ShopItem implements Cloneable {

	private int place, cost;
	private ItemStack item;
	private ShopItemType type;

	public ShopItem(int place, int cost, ItemStack item, ShopItemType type) {
		this.cost = cost;
		this.place = place;
		this.item = item;
		this.type = type;
	}

	public int getCost() {
		return cost;
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

	@Override
	public ShopItem clone() {
		try {
			ShopItem shopItem = (ShopItem) super.clone();
			shopItem.item = item.clone();

			return shopItem;
		} catch (Exception ex) {
			Bukkit.getLogger().log(Level.SEVERE, "[MCInfected] Error cloning ShopItem.", ex);
			return null;
		}
	}

	public enum ShopItemType {
		CLOSE, RANK_UP, ITEM, COOKIES
	}
}
