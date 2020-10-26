package net.urbanmc.mcinfected.manager;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.MysteryReward;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MysteryManager {

	private static MysteryManager instance = new MysteryManager();

	private List<MysteryReward> rewards;
	private RangeMap<Integer, MysteryReward> map;

	private MysteryManager() {
		loadRewards();
		loadIntoMap();
	}

	public static MysteryManager getInstance() {
		return instance;
	}

	private void loadRewards() {
		Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("mysterychest.yml"));
		FileConfiguration data = YamlConfiguration.loadConfiguration(reader);

		this.rewards = new ArrayList<>();

		for (String s : data.getStringList("items")) {
			String[] split = s.split(" ");

			int chance = Integer.parseInt(split[0]);

			split = split[1].split(";");

			List<ItemStack> rewards = new ArrayList<>();

			for (String item : split) {
				ItemStack itemStack = ItemUtil.getItem(item);

				if (itemStack != null)
					rewards.add(itemStack);
			}

			this.rewards.add(new MysteryReward(rewards, chance));
		}
	}

	private void loadIntoMap() {
		map = TreeRangeMap.create();

		int lowerBound = 0;

		for (MysteryReward reward : rewards) {
			Range<Integer> range = Range.closedOpen(lowerBound, lowerBound + reward.getChance());

			map.put(range, reward);

			lowerBound = lowerBound + reward.getChance();
		}
	}

	private MysteryReward getRandomReward() {
		Random r = ThreadLocalRandom.current();
		int chance = r.nextInt(100);

		return map.get(chance);
	}

	public void rewardPlayer(GamePlayer p) {
		MysteryReward reward = getRandomReward();

		List<ItemStack> items = reward.getRewards();

		ItemStack mainItem = items.get(0);

		String message;

		if (mainItem.getType().equals(Material.COOKIE)) {
			p.giveCookies(1000);
			Bukkit.broadcastMessage(Messages.getInstance()
					                        .getString("player_won_cookies", p.getOnlinePlayer().getName()));

			message = Messages.getInstance().getString("mystery_chest_win_cookies");
		} else {
			for (ItemStack is : items) {
				p.getOnlinePlayer().getInventory().addItem(is);
			}

			message = Messages.getInstance()
					.getString("mystery_chest_win", mainItem.getAmount(), ItemUtil.getFriendlyName(mainItem));
		}

		p.getOnlinePlayer().sendMessage(message);
	}
}
