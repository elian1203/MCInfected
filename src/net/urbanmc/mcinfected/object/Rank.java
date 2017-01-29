package net.urbanmc.mcinfected.object;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Rank {

    private int level;
    private String name;
    private ChatColor color;
    private long cost;
    private List<ItemStack> armor, items;

    public Rank(int level, String name, ChatColor color, long cost, List<ItemStack> items, List<ItemStack> armor) {
        this.level = level;
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.items = items;
        this.armor = armor;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public long getCost() {
        return cost;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public List<ItemStack> getArmor() {
        return armor;
    }
}
