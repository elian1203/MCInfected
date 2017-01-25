package net.urbanmc.mcinfected.object;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Rank {

    private String name;
    private int level;
    private List<ItemStack> armor;
    private List<ItemStack> items;

    public Rank(String name, int level, List<ItemStack> armor, List<ItemStack> items) {
        this.name = name;
        this.level = level;
        this.armor = armor;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public List<ItemStack> getArmor() {
        return armor;
    }

    public List<ItemStack> getItems() {
        return items;
    }
}
