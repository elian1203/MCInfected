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
import java.util.Random;

public class MapManager {

	private static MapManager instance = new MapManager();

	private FileConfiguration data;

	private Map lobby, random;
	private List<Map> maps, specific;

	public static MapManager getInstance() {
		return instance;
	}

	private MapManager() {
		data = YamlConfiguration.loadConfiguration(new File("plugins/MCInfected", "maps.yml"));

		loadLobby();
		loadMaps();
		loadSpecific();
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

	private void loadRandom() {
		Random r = new Random();

		random = maps.get(r.nextInt(maps.size()) - 1);
	}

	private void loadSpecific() {
		specific = new ArrayList<>();

		Random r = new Random();

		int size = maps.size();

		for (int i = 0; i < (size < 5 ? size : 5); i++) {
			int temp = r.nextInt(size);

			Map map = maps.get(temp - 1);

			if (specific.contains(map)) {
				i--;
				continue;
			}

			specific.add(map);
		}
	}

	public Map getLobby() {
		return lobby;
	}

	public List<Map> getMaps() {
		return maps;
	}

	public List<Map> getSpecific() {
		return specific;
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
			if (map.getName().equalsIgnoreCase(name))
				return map;
		}

		return null;
	}

	public Map getSpecificByName(String name) {
		for (Map map : specific) {
			if (map.getName().equalsIgnoreCase(name))
				return map;
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
