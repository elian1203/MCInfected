package net.urbanmc.mcinfected.object;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MysteryReward {

	private List<ItemStack> rewards;
	private int chance;

	public MysteryReward(List<ItemStack> rewards, int chance) {
		this.rewards = rewards;
		this.chance = chance;
	}

	public List<ItemStack> getRewards() {
		return rewards;
	}

	public int getChance() {
		return chance;
	}
}
