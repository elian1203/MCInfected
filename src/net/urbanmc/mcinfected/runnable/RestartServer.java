package net.urbanmc.mcinfected.runnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.MapManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RestartServer extends BukkitRunnable {

	private MCInfected plugin;

	public RestartServer(MCInfected plugin) {
		this.plugin = plugin;
		runTaskLater(plugin, 200);
	}

	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(MapManager.getInstance().getLobby().getSpawn());

			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF("Minigames");

			player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
		}

		GamePlayerManager.getInstance().savePlayers();
		MapManager.getInstance().cleanseMap();

		Bukkit.shutdown();
	}
}
