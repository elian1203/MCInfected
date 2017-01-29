package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapManager {

	private static MapManager instance = new MapManager();

	private FileConfiguration data;

	private Map lobby;
	private List<Map> maps;

	public static MapManager getInstance() {
		return instance;
	}

	private MapManager() {
		data = YamlConfiguration.loadConfiguration(new File("plugins/MCInfected", "maps.yml"));

		loadLobby();
		loadMaps();
	}

	private void loadLobby() {
		String loc = data.getString("lobby.spawn");

		lobby = new Map("#lobby", getLocation(loc));
	}

	private void loadMaps() {
		maps = new ArrayList<>();

		ConfigurationSection sect = data.getConfigurationSection("maps");

		for (String name : sect.getKeys(false)) {
			String loc = sect.getString(name + ".spawn");

			maps.add(new Map(name, getLocation(loc)));
		}
	}

	public Map getLobby() {
		return lobby;
	}

	public List<Map> getMaps() {
		return maps;
	}

	public List<String> getMapNames() {
		List<String> names = new ArrayList<>();

		for (Map map : MapManager.getInstance().getMaps()) {
			names.add(map.getName());
		}

		Collections.sort(names);

		return names;
	}

	public Map getMapByName(String name) {
		for (Map map : maps) {
			if (map.getName().equalsIgnoreCase(name)) return map;
		}

		return null;
	}

	private Location getLocation(String loc) {
		String[] split = loc.split("/");

		double x = Double.parseDouble(split[0]), y = Double.parseDouble(split[1]), z = Double.parseDouble(split[2]);
		float yaw = Float.parseFloat(split[3]), pitch = Float.parseFloat(split[4]);
		World world = Bukkit.getWorld(split[5]);

		return new Location(world, x, y, z, yaw, pitch);
	}
}
