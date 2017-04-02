package net.urbanmc.mcinfected.runnable;


import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.Messages;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable {

	private int time = 600;

	GameTimer(MCInfected plugin) {
		runTaskTimerAsynchronously(plugin, 0, 20);
	}

	@Override
	public void run() {

		if (time == 300 || time == 60)
			broadcastTime();

		if (time == 0) {
			GameManager.getInstance().endGame(false, null);
			cancel();
		}

		time--;
	}

	private void broadcastTime() {
		Bukkit.broadcastMessage(Messages.getInstance().getString("time_remaining", time / 60));
	}
}
