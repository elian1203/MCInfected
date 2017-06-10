package net.urbanmc.mcinfected.object;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KillStreak {

	private int kills;
	private int[] repeats;
	private List<ItemStack> rewards;
	private boolean zombieStreak;

	public KillStreak(int kills, int[] repeats, List<ItemStack> rewards, boolean zombieStreak) {
		this.kills = kills;
		this.repeats = repeats;
		this.rewards = rewards;
		this.zombieStreak = zombieStreak;
	}

	public int getKills() {
		return kills;
	}

	public int[] getRepeats() {
		return repeats;
	}

	public List<ItemStack> getRewards() {
		return rewards;
	}

	public boolean isZombieStreak() {
		return zombieStreak;
	}

	private ItemStack[] getRewardsAsArray() {
		ItemStack[] array = new ItemStack[rewards.size()];
		array = rewards.toArray(array);

		return array;
	}

	public void giveKillStreak(GamePlayer p) {
		p.getOnlinePlayer().getInventory().addItem(getRewardsAsArray());
	}
}
