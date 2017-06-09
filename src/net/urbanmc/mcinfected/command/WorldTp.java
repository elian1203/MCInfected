package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class WorldTp extends Command {

	public WorldTp() {
		super("worldtp", "command.worldtp", true, Collections.singletonList("wtp"));
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		if (args.length == 0) {
			messagePlayer(p, "/" + label + " [world]");
			return;
		}

		if (!MapManager.getInstance().isExistentWorld(args[0])) {
			messagePlayer(p, color("&cWorld does not exist."));
			return;
		}

		loadWorld(args[0]);

		World world = Bukkit.getWorld(args[0]);

		p.getOnlinePlayer().teleport(world.getSpawnLocation());
	}

	private void loadWorld(String world) {
		World bukkitWorld = Bukkit.getWorld(world);

		if (bukkitWorld == null) {
			WorldCreator creator = new WorldCreator(world);
			creator.createWorld();
		}
	}
}
