package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.MapManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RestartServer extends BukkitRunnable {

	public RestartServer(MCInfected plugin) {
		runTaskLater(plugin, 200);
	}

	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(MapManager.getInstance().getLobby().getSpawn());
			player.kickPlayer("Restarting server. Be back in a bit!");
		}

		GamePlayerManager.getInstance().savePlayers();
		MapManager.getInstance().cleanseMap();

		Bukkit.shutdown();
	}
}
