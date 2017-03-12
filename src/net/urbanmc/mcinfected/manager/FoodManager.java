package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.Food;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FoodManager {

    private static FoodManager instance = new FoodManager();

    private List<Food> food;

    private FoodManager() {

        loadFood();
    }

    private void loadFood() {
        FileConfiguration data = YamlConfiguration.loadConfiguration(new File("plugins/MCInfected", "food.yml"));
        food = new ArrayList<>();

        List<String> foodStringList = data.getStringList("food");

        for (String string : foodStringList) {
            String[] foodSplit = string.split(":");

            Material mat = Material.valueOf(foodSplit[0].toUpperCase());
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

    public static FoodManager getInstance() {
        return instance;
    }

}
