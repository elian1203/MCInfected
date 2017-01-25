package net.urbanmc.mcinfected.object;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KillStreak {

    private String name;
    private int kills;
    private boolean repeatable;
    private List<ItemStack> rewards;

    public KillStreak(String name, int kills, boolean repeatable, List<ItemStack> rewards) {
        this.name = name;
        this.kills = kills;
        this.repeatable = repeatable;
        this.rewards = rewards;
    }

    public String getName() {
        return name;
    }

    public int getKills() {
        return kills;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public List<ItemStack> getRewards() {
        return rewards;
    }
}
