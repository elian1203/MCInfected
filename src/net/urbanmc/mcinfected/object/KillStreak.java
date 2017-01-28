package net.urbanmc.mcinfected.object;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KillStreak {

    private int kills;
    private int[] repeats;
    private List<ItemStack> rewards;

    public KillStreak(int kills, int[] repeats, List<ItemStack> rewards) {
        this.kills = kills;
        this.repeats = repeats;
        this.rewards = rewards;
    }

    public int getKills() {
        return kills;
    }

    public int[] isRepeatable() {
        return repeats;
    }

    public List<ItemStack> getRewards() {
        return rewards;
    }
}
