package net.urbanmc.mcinfected.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemUtil {

    public static ItemStack getItem(String name) {
        String[] split = name.split(" ");

        ItemStack is = new ItemStack(Material.getMaterial(split[0].toUpperCase()));

        if (split.length == 0) {
            return is;
        }

        for (String arg : split) {
            if (arg.startsWith("amount:")) {
                int amount = Integer.parseInt(arg.substring(7));
                is.setAmount(amount);
                continue;
            }

            if (arg.startsWith("enchant:")) {
                String enchant = arg.substring(8);
                String[] enchantSplit = enchant.split("/");

                Enchantment ench = Enchantment.getByName(enchantSplit[0].toUpperCase());
                int level = enchantSplit.length == 1 ? 1 : Integer.parseInt(enchantSplit[1]);

                ItemMeta meta = is.getItemMeta();

                meta.addEnchant(ench, level, true);

                is.setItemMeta(meta);
                continue;
            }

            if (arg.startsWith("effect:")) {
                String effect = arg.substring(10);
                String[] effectSplit = effect.split("/");

                PotionEffectType effectType = PotionEffectType.getByName(effectSplit[0]);
                int level = effectSplit.length < 2 ? 1 : Integer.parseInt(effectSplit[1]);
                int duration = effectSplit.length < 3 ? 1 : Integer.parseInt(effectSplit[2]);

                PotionMeta meta = (PotionMeta) is.getItemMeta();

                meta.addCustomEffect(new PotionEffect(effectType, duration, level), true);

                is.setItemMeta(meta);
            }
        }

        return is;
    }

    public static List<ItemStack> getItemList(List<String> list) {
        List<ItemStack> items = new ArrayList<>();

        for (String s : list) {
            items.add(getItem(s));
        }

        return items;
    }

    public static void equipPlayer(Player p, List<ItemStack> items, List<ItemStack> armor) {
        for (ItemStack item : items) {
            p.getInventory().addItem(item);
        }

        Collections.reverse(armor);

        ItemStack[] array = new ItemStack[4];
        array = armor.toArray(array);

        p.getInventory().setArmorContents(array);
    }
}
