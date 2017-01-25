package net.urbanmc.mcinfected.object;

import org.bukkit.Location;

public class Map {

    private String name;
    private Location spawn;

    public Map(String name, Location spawn) {
        this.name = name;
        this.spawn = spawn;
    }

    public String getName() {
        return this.name;
    }

    public Location getSpawn() {
        return this.spawn;
    }
}
