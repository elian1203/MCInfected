package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.KillStreak;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KillStreakManager {

	private static KillStreakManager instance = new KillStreakManager();

	private List<KillStreak> killStreaks;

	public static KillStreakManager getInstance() {
		return instance;
	}

	private KillStreakManager() {
		loadKillStreaks();
	}

	private void loadKillStreaks() {
		killStreaks = new ArrayList<>();

		FileConfiguration data = YamlConfiguration.loadConfiguration(new File(
				"plugins/MCInfected",
				"killstreaks" + ".yml"));

		loadKillStreaksFromSection(data.getConfigurationSection("streaks.human"), false);
		loadKillStreaksFromSection(data.getConfigurationSection("streaks.zombie"), true);
	}

	private void loadKillStreaksFromSection(ConfigurationSection sect, boolean zombieStreaks) {
		for (String amount : sect.getKeys(false)) {
			List<Integer> list = sect.getIntegerList(amount + ".repeats");

			int[] repeats = list.stream().mapToInt(i -> i).toArray();

			List<ItemStack> rewards = ItemUtil.getItemList(sect.getStringList(amount + ".items"));

			killStreaks.add(new KillStreak(Integer.parseInt(amount), repeats, rewards, zombieStreaks));
		}
	}

	public List<KillStreak> getKillStreaks() {
		return killStreaks;
	}

	public KillStreak getKillStreak(int kills, boolean zombieStreak) {
		for (KillStreak killStreak : killStreaks) {
			if (killStreak.getKills() == kills && killStreak.isZombieStreak() == zombieStreak)
				return killStreak;

			for (int repeat : killStreak.getRepeats()) {
				if (repeat == kills && killStreak.isZombieStreak() == zombieStreak)
					return killStreak;
			}
		}

		return null;
	}
}
