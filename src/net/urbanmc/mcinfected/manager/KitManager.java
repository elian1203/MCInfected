package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.Kit;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class KitManager {

	private static KitManager instance = new KitManager();

	private FileConfiguration data;
	private List<Kit> kits;
	private Kit zombie, mother;

	private KitManager() {
		Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("kits.yml"));
		data = YamlConfiguration.loadConfiguration(reader);

		loadMotherAndZombie();
		loadKits();
	}

	public static KitManager getInstance() {
		return instance;
	}

	private void loadKits() {
		kits = new ArrayList<>();

		ConfigurationSection sect = data.getConfigurationSection("kits");

		for (String name : sect.getKeys(false))
			kits.add(formKit(name));

	}

	private void loadMotherAndZombie() {
		mother = formKit("mother");
		zombie = formKit("zombie");
	}

	private Kit formKit(String path) {
		EntityType disguise = null;

		if (data.contains(path + ".disguise")) {
			disguise = EntityType.valueOf(data.getString(path + ".disguise").toUpperCase());
		}

		return new Kit(
				data.getString(path + ".name"),
				data.getString(path + ".permission"),
				ItemUtil.getItemList(data.getStringList(path + ".armor")),
				ItemUtil.getItemList(data.getStringList(path + ".items")),
				disguise);
	}

	public List<Kit> getKits() {
		return kits;
	}

	public Kit getZombieKit() {
		return zombie;
	}

	public Kit getMotherKit() {
		return mother;
	}
}
