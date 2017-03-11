package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.Map;
import org.bukkit.*;
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

	private Map lobby, random, current;
	private List<Map> maps, specific;

	public static MapManager getInstance() {
		return instance;
	}

	private MapManager() {
		data = YamlConfiguration.loadConfiguration(new File("plugins/MCInfected", "maps.yml"));

		loadLobby();
		loadMaps();
		loadRandom();
		loadSpecific();
	}

	private void loadLobby() {
		String loc = data.getString("lobby.spawn");

		lobby = new Map("#lobby", loc);
	}

	private void loadMaps() {
		maps = new ArrayList<>();

		ConfigurationSection sect = data.getConfigurationSection("maps");

		for (String name : sect.getKeys(false)) {
			String loc = sect.getString(name + ".spawn");

			maps.add(new Map(name, loc));
		}
	}

	private void loadRandom() {
		Random r = new Random();

		System.out.println(maps.size());

		int i = r.nextInt(maps.size());

		System.out.println(i);

		i--;

		System.out.println(i);

		random = maps.get(i);
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

	public void setLobbyChars() {
		World world = Bukkit.getWorld("lobby");

		world.setPVP(false);
		world.setGameRuleValue("doMobSpawning", "false");
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

	public Map getRandom() {
		return random;
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
		if (name.equalsIgnoreCase("random"))
			return random;

		for (Map map : specific) {
			if (map.getName().equalsIgnoreCase(name))
				return map;
		}

		return null;
	}

	public void loadMap(Map map) {
		WorldCreator creator = new WorldCreator(map.getWorld());

		creator.createWorld();

		World world = Bukkit.getWorld(map.getWorld());

		world.setPVP(true);
		world.setDifficulty(Difficulty.HARD);

		world.setGameRuleValue("doMobSpawning", "false");
		world.setGameRuleValue("doMobLoot", "false");
		world.setGameRuleValue("doDaylightCycle", "true");
	}

	public Map getGameMap() {
		return current;
	}

	public void setGameMap(Map map) {
		current = map;
	}
}
