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

    private FileConfiguration data;
    private List<Food> food;

    private FoodManager() {
        data = YamlConfiguration.loadConfiguration(new File("plugins/MCInfected", "food.yml"));

        loadFood();
    }

    private void loadFood() {
        food = new ArrayList<>();
        List<String> foodStringList = data.getStringList("food");

        String[] foodSplit;
        int healthReplenished;
        Material mat;

        for(String string : foodStringList) {
            foodSplit = string.split(":");

            mat = Material.valueOf(foodSplit[0].toUpperCase());
            healthReplenished = Integer.parseInt(foodSplit[1]);

            food.add(new Food(mat, healthReplenished));
        }
    }

    public List<Food> getFoodList() {
        return food;
    }

    public static FoodManager getInstance() {
        return instance;
    }

}
