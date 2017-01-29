package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.Rank;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RankManager {

	private static RankManager instance = new RankManager();

	private List<Rank> ranks;

	public static RankManager getInstance() {
		return instance;
	}

	private RankManager() {
		loadRanks();
	}

	private void loadRanks() {
		ranks = new ArrayList<Rank>();

		FileConfiguration data = YamlConfiguration.loadConfiguration(new File("plugins/MCInfected", "ranks.yml"));
		ConfigurationSection sect = data.getConfigurationSection("ranks");

		for (String level : sect.getKeys(false)) {
			String name = sect.getString(level + ".name");
			ChatColor color = ChatColor.getByChar(sect.getString(level + ".color").charAt(0));
			long cost = sect.getLong(level + ".cost");

			List<ItemStack> items = ItemUtil.getItemList(sect.getStringList(level + ".items"));
			List<ItemStack> armor = ItemUtil.getItemList(sect.getStringList(level + ".armor"));

			ranks.add(new Rank(Integer.parseInt(level), name, color, cost, items, armor));
		}
	}

	public List<Rank> getRanks() {
		return ranks;
	}
}
