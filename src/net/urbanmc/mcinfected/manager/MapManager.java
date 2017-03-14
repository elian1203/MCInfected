package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.Map;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MapManager {

	private static MapManager instance = new MapManager();

	private FileConfiguration data;

	private Map lobby, random, current;
	private List<Map> maps;

	private List<Map> specific;

	private MapManager() {
		Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("maps.yml"));
		data = YamlConfiguration.loadConfiguration(reader);

		loadLobby();
		loadMaps();
		loadRandom();
		loadSpecific();
	}

	public static MapManager getInstance() {
		return instance;
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
		random = new Map("#random", "0/0/0/0/0/random");
	}

	private void loadSpecific() {
		specific = new ArrayList<>();

		Random r = new Random();

		int size = maps.size();

		for (int i = 0; i < 4; i++) {
			int index = r.nextInt(size);

			Map map = maps.get(index);

			if (specific.contains(map)) {
				i--;
				continue;
			}

			specific.add(map);
		}

		specific.sort((m1, m2) -> m1.getName().compareTo(m2.getName()));
	}

	public void setLobbyVars() {
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

	public Map getRealRandom() {
		Random r = new Random();

		int i = r.nextInt(maps.size());

		return maps.get(i);
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

	public Map getSpecificByIndex(int index) {
		if (index > 4)
			return null;

		if (index == 4)
			return random;

		return specific.get(index);
	}

	public Map getSpecificByName(String name) {
		if (name.equalsIgnoreCase("random"))
			return random;

		for (Map map : specific) {
			if (map.getName().toLowerCase().startsWith(name.toLowerCase()))
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
		world.setGameRuleValue("doDaylightCycle", "false");
	}

	public Map getGameMap() {
		return current;
	}

	public void setGameMap(Map map) {
		current = map;
	}
}
