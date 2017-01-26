package net.urbanmc.mcinfected.object;

import org.bukkit.Location;

public class Map {

    private String name;
    private Location spawn;
    private int votes;

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

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
