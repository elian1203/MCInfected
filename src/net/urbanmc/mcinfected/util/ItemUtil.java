package net.urbanmc.mcinfected.util;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.Kit;
import net.urbanmc.mcinfected.object.grenade.*;
import net.urbanmc.mcinfected.runnable.ItemThrown;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
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

				PotionEffectType effectType = PotionEffectType.getByName(effectSplit[0].toUpperCase());
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

	public static void equipPlayer(GamePlayer p) {
		Player player = p.getOnlinePlayer();

		player.getInventory().clear();
		player.getInventory().setArmorContents(null);

		Kit kit = p.getKit();

		List<ItemStack> items = kit.getItems();
		List<ItemStack> armor = kit.getArmor();

		for (ItemStack item : items) {
			player.getInventory().addItem(item);
		}

		Collections.reverse(armor);

		ItemStack[] array = new ItemStack[4];
		array = armor.toArray(array);

		player.getInventory().setArmorContents(array);

		if (kit.hasDisguise()) {
			DisguiseUtil.disguisePlayerAsEntity(player, kit.getDisguise());
		}
	}

	public static boolean checkSpace(Player p, ItemStack is) {
		if (p.getInventory().firstEmpty() < 36 && p.getInventory().firstEmpty() != -1)
			return true;

		for (ItemStack item : p.getInventory()) {
			if (item == null)
				continue;

			if (item.getType() != is.getType())
				continue;

			if (item.getDurability() != is.getDurability())
				continue;

			if (item.getAmount() + is.getAmount() > 64)
				continue;

			return true;
		}

		return false;
	}

	public static void throwItem(Player p, ItemStack is, MCInfected plugin) {
		p.getInventory().removeItem(is);

		Item item = p.getWorld().dropItem(p.getLocation(), is);

		Grenade grenade = Grenade.parseGrenade(item);

		item.setVelocity(p.getLocation().getDirection().normalize().multiply(2.5));

		ItemThrown runnable = new ItemThrown(grenade);

		int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 0, 1);

		runnable.setTaskId(taskId);
	}
}
