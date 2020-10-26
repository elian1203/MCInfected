package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.Food;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class FoodManager {

	private static FoodManager instance = new FoodManager();

	private List<Food> food;

	private List<Block> cakes;

	private FoodManager() {
		cakes = new ArrayList<>();
		loadFood();
	}

	public static FoodManager getInstance() {
		return instance;
	}

	private void loadFood() {
		Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("food.yml"));
		FileConfiguration data = YamlConfiguration.loadConfiguration(reader);

		food = new ArrayList<>();

		List<String> foodStringList = data.getStringList("food");

		for (String string : foodStringList) {
			String[] foodSplit = string.split(":");

			Material mat = Material.getMaterial(foodSplit[0].toUpperCase());

			if (mat == null) {
				Bukkit.getLogger().warning("[MCInfected] Cannot parse food '" + foodSplit[0] + "' !");
				continue;
			}

			int healthReplenished = Integer.parseInt(foodSplit[1]);

			food.add(new Food(mat, healthReplenished));
		}
	}

	public List<Food> getFoodList() {
		return food;
	}

	public Food getFoodByMaterial(Material type) {
		for (Food food : food) {
			if (food.getMaterial().equals(type))
				return food;
		}

		return null;
	}

	public List<Block> getCakes() {
		return cakes;
	}
}
