package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.manager.GameManager.GameState;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.Rank;
import net.urbanmc.mcinfected.object.ShopItem;
import net.urbanmc.mcinfected.object.ShopItem.ShopItemType;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopManager {

	public static ShopManager instance = new ShopManager();

	private List<ShopItem> items;

	private ShopManager() {
		loadShopItems();
	}

	public static ShopManager getInstance() {
		return instance;
	}

	private void loadShopItems() {
		items = new ArrayList<>();

		Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("shopitems.yml"));
		FileConfiguration data = YamlConfiguration.loadConfiguration(reader);

		ConfigurationSection sect = data.getConfigurationSection("items");

		for (String key : sect.getKeys(false)) {
			int place = Integer.parseInt(key), cost = sect.getInt(key + ".cost");
			ItemStack item = ItemUtil.getItem(sect.getString(key + ".item"));
			ShopItemType type = ShopItemType.valueOf(sect.getString(key + ".type").toUpperCase());

			items.add(new ShopItem(place, cost, item, type));
		}
	}

	public List<ShopItem> getItems() {
		return items;
	}

	public Inventory getShop(GamePlayer p) {
		Inventory shop = Bukkit.createInventory(null, 9, "Shop");

		for (int i = 0; i < 9; i++) {
			ShopItem configItem = getShopItem(i);

			if (configItem == null)
				continue;

			ShopItem item = configItem.clone();

			if (item.getType().equals(ShopItemType.RANK_UP)) {
				ItemMeta meta = item.getItem().getItemMeta();
				Rank rank = RankManager.getInstance().getRankByLevel(p.getRank().getLevel() + 1);

				if (rank == null) {
					meta.setDisplayName(ChatColor.GOLD + "You are max level");
					meta.setLore(Collections.emptyList());
				} else {
					String cost = Long.toString(rank.getCost());

					String lore = meta.getLore().get(0).replace("%rank%", rank.getName()).replace("%amount%", cost);
					meta.setLore(Collections.singletonList(lore));
				}

				item.getItem().setItemMeta(meta);
			} else if (item.getType().equals(ShopItemType.COOKIES)) {
				ItemMeta meta = item.getItem().getItemMeta();

				String lore = meta.getLore().get(0).replace("%amount%", Long.toString(p.getCookies()));
				meta.setLore(Collections.singletonList(lore));

				item.getItem().setItemMeta(meta);
			}

			shop.setItem(i, item.getItem());
		}

		return shop;
	}

	public ShopItem getShopItem(int place) {
		for (ShopItem item : items) {
			if (item.getPlace() == place)
				return item;
		}

		return null;
	}

	public synchronized void manageClickedItem(GamePlayer p, ShopItem item) {
		ShopItemType type = item.getType();

		switch (type) {
			case CLOSE:
				p.getOnlinePlayer().closeInventory();
				break;
			case RANK_UP:
				rankupPlayer(p);
				break;
			case ITEM:
				purchaseItem(p, item);
				break;
		}
	}

	private void rankupPlayer(GamePlayer p) {
		int level = p.getRank().getLevel();

		if (level == 20) {
			p.getOnlinePlayer().sendMessage(Messages.getInstance().getString("max_rank"));
			return;
		}

		Rank rank = RankManager.getInstance().getRankByLevel(level + 1);

		long cost = rank.getCost();

		if (p.getCookies() < cost) {
			p.getOnlinePlayer().sendMessage(Messages.getInstance().getString("insufficient_cookies"));
			return;
		}

		p.setCookies(0);

		p.setRank(rank);

		String message = Messages.getInstance()
				.getString("player_rank_up", p.getOnlinePlayer().getName(), rank.getColor().getChar(), rank.getName());

		Bukkit.broadcastMessage(message);

		p.getOnlinePlayer().closeInventory();
	}

	private void purchaseItem(GamePlayer p, ShopItem item) {
		if (GameManager.getInstance().getGameState() != GameState.RUNNING) {
			p.getOnlinePlayer().sendMessage(Messages.getInstance().getString("cannot_buy_yet"));
			return;
		}

		int cost = item.getCost();

		if (p.getCookies() < cost) {
			p.getOnlinePlayer().sendMessage(Messages.getInstance().getString("insufficient_cookies"));
			return;
		}

		ItemStack is = item.getItem().clone();

		ItemMeta meta = is.getItemMeta();
		meta.setLore(new ArrayList<>());

		is.setItemMeta(meta);

		Player player = p.getOnlinePlayer();

		if (!ItemUtil.checkSpace(player, is)) {
			player.sendMessage(Messages.getInstance().getString("insufficient_space"));
			return;
		}

		p.setCookies(p.getCookies() - cost);

		player.getInventory().addItem(is);
		player.sendMessage(Messages.getInstance()
				                   .getString("bought_item", is.getAmount(), ItemUtil.getFriendlyName(is), cost));
	}
}
