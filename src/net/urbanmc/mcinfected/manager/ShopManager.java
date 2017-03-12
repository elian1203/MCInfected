package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.ShopItem;

import java.util.List;

public class ShopManager {

	public static ShopManager instance = new ShopManager();

	private List<ShopItem> items;

	public static ShopManager getInstance() {
		return instance;
	}
}
