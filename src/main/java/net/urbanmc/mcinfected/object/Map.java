package net.urbanmc.mcinfected.object;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Map {

	private String name, world;
	private String spawn;
	private int votes = 0;

	public Map(String name, String spawn) {
		this.name = name;
		this.spawn = spawn;
	}

	public String getName() {
		return name;
	}

	public Location getSpawn() {
		String[] split = spawn.split("/");

		double x = Double.parseDouble(split[0]), y = Double.parseDouble(split[1]), z = Double.parseDouble(split[2]);
		float yaw = Float.parseFloat(split[3]), pitch = Float.parseFloat(split[4]);
		World world = Bukkit.getWorld(split[5]);

		return new Location(world, x, y, z, yaw, pitch).clone();
	}

	public int getVotes() {
		return votes;
	}

	public String getWorld() {
		return spawn.split("/")[5];
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
}
