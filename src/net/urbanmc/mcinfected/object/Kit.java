package net.urbanmc.mcinfected.object;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kit {

	private String name, permission;
	private List<ItemStack> armor, items;
	private EntityType disguise;

	public Kit(String name, String permission, List<ItemStack> armor, List<ItemStack> items, EntityType disguise) {
		this.name = name;
		this.permission = permission;
		this.armor = armor;
		this.items = items;
		this.disguise = disguise;
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

	public boolean hasDisguise() {
		return disguise != null;
	}

	public EntityType getDisguise() {
		return disguise;
	}

	public boolean playerHasAccess(GamePlayer p) {
		return p.getOnlinePlayer().hasPermission(permission);
	}
}
