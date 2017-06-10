package net.urbanmc.mcinfected.util;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.Kit;
import net.urbanmc.mcinfected.object.grenade.Grenade;
import net.urbanmc.mcinfected.runnable.ItemThrown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class ItemUtil {

	public static ItemStack getItem(String name) {
		String[] split = name.split(" ");

		ItemStack is = new ItemStack(Material.getMaterial(split[0].toUpperCase()));

		ItemMeta meta = is.getItemMeta();
		meta.setUnbreakable(true);

		if (split.length == 0) {
			return is;
		}

		for (String arg : split) {
			if (arg.startsWith("name:")) {
				String displayName = ChatColor.translateAlternateColorCodes('&', arg.substring(5).replace("_", " "));
				meta.setDisplayName(ChatColor.RESET + displayName);

				continue;
			}

			if (arg.startsWith("lore:")) {
				String lore = ChatColor.translateAlternateColorCodes('&', arg.substring(5).replace("_", " "));
				meta.setLore(Collections.singletonList(lore));

				continue;
			}

			if (arg.startsWith("amount:")) {
				int amount = Integer.parseInt(arg.substring(7));
				is.setAmount(amount);

				continue;
			}

			if (arg.startsWith("enchant:")) {
				String enchant = arg.substring(8);
				String[] enchantSplit = enchant.split("/");

				enchantSplit[0] = convertEnchants(enchantSplit[0]);

				Enchantment ench = Enchantment.getByName(enchantSplit[0].toUpperCase());
				int level = enchantSplit.length == 1 ? 1 : Integer.parseInt(enchantSplit[1]);

				meta.addEnchant(ench, level, true);

				continue;
			}

			if (arg.startsWith("effect:")) {
				String effect = arg.substring(7);
				String[] effectSplit = effect.split("/");

				PotionEffectType effectType = PotionEffectType.getByName(effectSplit[0].toUpperCase());

				int level = effectSplit.length < 2 ? 1 : Integer.parseInt(effectSplit[1]);
				int duration = effectSplit.length < 3 ? 1 : Integer.parseInt(effectSplit[2]);

				PotionMeta potionMeta = (PotionMeta) meta;

				potionMeta.addCustomEffect(new PotionEffect(effectType, duration, level), true);
			}
		}

		is.setItemMeta(meta);

		return is;
	}

	private static String convertEnchants(String s) {
		switch (s) {
			case "sharpness":
				return "damage_all";
			case "infinity":
				return "arrow_infinite";
			case "power":
				return "arrow_damage";
			case "punch":
				return "arrow_knockback";
		}
		return s;
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

		List<ItemStack> items;
		List<ItemStack> armor;

		Kit kit = p.getKit();

		if (kit == null) {
			items = p.getRank().getItems();
			armor = p.getRank().getArmor();
		} else {
			items = kit.getItems();
			armor = kit.getArmor();
		}

		for (ItemStack item : items) {
			player.getInventory().addItem(item);
		}

		Collections.reverse(armor);
		List<Material> armorlist = Arrays.asList(
				Material.CHAINMAIL_HELMET,
				Material.LEATHER_HELMET,
				Material.IRON_HELMET,
				Material.GOLD_HELMET,
				Material.DIAMOND_HELMET);
		if (!armorlist.contains(armor.get(armor.size() - 1).getType()))
			Collections.reverse(armor);
		ItemStack[] array = new ItemStack[4];
		array = armor.toArray(array);

		player.getInventory().setArmorContents(array);

		if (kit != null && kit.hasDisguise()) {
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

	public static void throwItem(GamePlayer p, ItemStack is, MCInfected plugin) {
		Player player = p.getOnlinePlayer();
		if (is.getAmount() > 1)
			is.setAmount(is.getAmount() - 1);
		else
			player.getInventory().removeItem(is);

		Item item = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()), is);

		Grenade grenade = Grenade.parseGrenade(p, item);

		item.setPickupDelay(1000000);

		item.setVelocity(player.getLocation().getDirection().multiply(4));

		new ItemThrown(grenade, plugin);
	}

	@SuppressWarnings("ConstantConditions")
	public static String getFriendlyName(ItemStack is) {
		if (is.getItemMeta().hasDisplayName())
			return is.getItemMeta().getDisplayName();

		try {
			Object nmsStack = getCraftItemStackClass().getMethod("asNMSCopy", ItemStack.class).invoke(null, is);

			return nmsStack.getClass().getMethod("getName").invoke(nmsStack).toString();
		} catch (Exception ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Error getting friendly name for " + is.getType().toString(), ex);
			return "";
		}
	}

	private static Class<?> getCraftItemStackClass() {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

		try {
			return Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftItemStack");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
