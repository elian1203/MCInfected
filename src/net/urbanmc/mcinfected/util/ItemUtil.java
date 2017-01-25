package net.urbanmc.mcinfected.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {

    // TODO: Finish getItem() method
    public static ItemStack getItem(String name) {
        return new ItemStack(Material.getMaterial(name));
    }
}
