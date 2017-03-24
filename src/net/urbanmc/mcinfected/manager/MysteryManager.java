package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.MysteryReward;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class MysteryManager {

	private static MysteryManager instance = new MysteryManager();

	private List<MysteryReward> rewards;

	private MysteryManager() {
		loadRewards();
	}

	public static MysteryManager getInstance() {
		return instance;
	}

	private void loadRewards() {
		Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("mysterychest.yml"));
		FileConfiguration data = YamlConfiguration.loadConfiguration(reader);

		for (String s : data.getStringList("items")) {
			String[] split = s.split(" ");

			int chance = Integer.parseInt(split[0]);

			split = split[1].split(";");

			List<ItemStack> rewards = new ArrayList<>();

			for (String item : split) {
				rewards.add(ItemUtil.getItem(item));
			}

			this.rewards.add(new MysteryReward(rewards, chance));
		}
	}

	private void getRandomReward() {

	}
}
