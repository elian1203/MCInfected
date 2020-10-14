package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.Map;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MapManager {

	private static MapManager instance = new MapManager();

	private File FILE = new File("plugins/MCInfected", "maps.yml");
	private FileConfiguration data;

	private Map lobby, random, current;
	private List<Map> maps;

	private List<Map> specific;

	private MapManager() {
		createFile();
		loadFile();
		loadLobby();
		loadMaps();
		loadRandom();
		loadSpecific();
		setLobbyVars();
	}

	private void createFile() {
		if (!FILE.exists()) {
			try {
				if (!FILE.getParentFile().isDirectory()) {
					FILE.getParentFile().mkdir();
				}

				FILE.createNewFile();

				InputStream input = getClass().getClassLoader().getResourceAsStream("maps.yml");
				OutputStream output = new FileOutputStream(FILE);
				copy(input, output);

				input.close();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadFile() {
		data = YamlConfiguration.loadConfiguration(FILE);
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
		random = new Map("Random", "0/0/0/0/0/random");
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

		specific.sort(Comparator.comparing(Map::getName));
	}

	private void setLobbyVars() {
		World world = Bukkit.getWorld("lobby");

		world.setPVP(false);
		world.setGameRuleValue("doMobSpawning", "false");
	}

	public Map getLobby() {
		return lobby;
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

	public void cleanseMap() {
		for (Block b : FoodManager.getInstance().getCakes()) {
			b.setType(Material.AIR);
		}

		FoodManager.getInstance().getCakes().clear();
	}

	public boolean isExistentWorld(String world) {
		File worldDir = new File(world);

		if (!worldDir.isDirectory())
			return false;

		boolean hasLevel = false;

		if (worldDir.listFiles() == null)
			return false;

		for (File child : worldDir.listFiles()) {
			if (child.getName().equals("level.dat")) {
				hasLevel = true;
				break;
			}
		}

		return hasLevel;
	}

	private int copy(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[1024 * 4];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}

		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}
}
