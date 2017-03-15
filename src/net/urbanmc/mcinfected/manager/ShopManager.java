package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.ShopItem;
import net.urbanmc.mcinfected.object.ShopItem.ShopItemType;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class ShopManager {

	public static ShopManager instance = new ShopManager();

	private List<ShopItem> items;

	private Inventory shop;

	private ShopManager() {
		loadShopItems();
		loadShop();
	}

	public static ShopManager getInstance() {
		return instance;
	}

	private void loadShopItems() {
		Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("shopitems.yml"));
		FileConfiguration data = YamlConfiguration.loadConfiguration(reader);

		for (String key : data.getConfigurationSection("items").getKeys(false)) {
			int place = Integer.parseInt(key);
			ItemStack item = ItemUtil.getItem(data.getString(key + ".item"));
			ShopItemType type = ShopItemType.valueOf(data.getString(key + ".type").toUpperCase());

			items.add(new ShopItem(place, item, type));
		}
	}

	private void loadShop() {
		shop = Bukkit.createInventory(null, 9, "Shop");

		for (ShopItem item : items) {
			shop.setItem(item.getPlace(), item.getItem());
		}
	}

	public List<ShopItem> getItems() {
		return items;
	}

	public Inventory getShop() {
		return shop;
	}

	public ShopItem getShopItem(int place) {
		for (ShopItem item : items) {
			if (item.getPlace() == place)
				return item;
		}

		return null;
	}

	public void manageClickedItem(GamePlayer p, ShopItem item) {

	}
}
