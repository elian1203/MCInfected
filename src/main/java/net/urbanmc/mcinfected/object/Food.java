package net.urbanmc.mcinfected.object;

import org.bukkit.Material;

public class Food {

    private Material material;
    private int healthReplenished;

    public Food(Material mat, int healthReplenished) {
        this.material = mat;
        this.healthReplenished = healthReplenished;
    }

    public Material getMaterial() {
        return material;
    }

    public int getHealthReplenished() {
        return healthReplenished;
    }


}
