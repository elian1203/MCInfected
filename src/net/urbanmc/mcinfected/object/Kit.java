package net.urbanmc.mcinfected.object;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kit {

    private String name, permission;
    private List<ItemStack> armor, items;

    public Kit(String name, String permission, List<ItemStack> armor, List<ItemStack> items) {
        this.name = name;
        this.permission = permission;
        this.armor = armor;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public List<ItemStack> getArmor() {
        return armor;
    }

    public List<ItemStack> getItems() {
        return items;
    }
}
