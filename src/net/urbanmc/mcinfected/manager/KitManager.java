package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.Kit;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KitManager {

	private static KitManager instance = new KitManager();

	private FileConfiguration data;
	private List<Kit> kits;
	private Kit zombie, mother;

	private KitManager() {
		Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("kits.yml"));
		data = YamlConfiguration.loadConfiguration(reader);

		loadKits();
		loadMotherAndZombie();
	}

	public static KitManager getInstance() {
		return instance;
	}

	private void loadKits() {
		kits = new ArrayList<>();

		ConfigurationSection sect = data.getConfigurationSection("kits");

		for (String name : sect.getKeys(false))
			kits.add(formKit(sect, name));

	}

	private void loadMotherAndZombie() {
		mother = formKit(data, "mother");
		zombie = formKit(data, "zombie");

		kits.add(mother);
		kits.add(zombie);
	}

	private Kit formKit(ConfigurationSection sect, String path) {
		EntityType disguise = null;

		if (sect.contains(path + ".disguise")) {
			disguise = EntityType.valueOf(sect.getString(path + ".disguise").toUpperCase());
		}

		return new Kit(
				sect.getString(path + ".name"),
				sect.getString(path + ".permission"),
				ItemUtil.getItemList(sect.getStringList(path + ".armor")),
				ItemUtil.getItemList(sect.getStringList(path + ".items")),
				disguise);
	}

	public Kit getZombieKit() {
		return zombie;
	}

	public Kit getMotherKit() {
		return mother;
	}

	public Kit getKitByName(String name) {
		for (Kit kit : kits) {
			if (kit.getName().equalsIgnoreCase(name)) {
				return kit;
			}
		}

		return null;
	}

	public List<Kit> getKitsForPlayer(GamePlayer p) {
		Player player = p.getOnlinePlayer();

		List<Kit> allowedKits =
				kits.stream().filter(kit -> kit.getPermission() == null || player.hasPermission(kit.getPermission()))
						.collect(Collectors.toList());

		if (!p.isMotherZombie()) {
			allowedKits.remove(mother);
		}

		return allowedKits;
	}
}
