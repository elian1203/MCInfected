package net.urbanmc.mcinfected.runnable;


import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.manager.ScoreboardManager;
import net.urbanmc.mcinfected.manager.ScoreboardManager.BoardType;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable {

	private static GameTimer instance = new GameTimer();

	private int time = 600;

	private GameTimer() {
	}

	public static void start(MCInfected plugin) {
		start(plugin, 600);
	}

	public static void start(MCInfected plugin, int seconds) {
		if (seconds <= 0)
			seconds = 600;

		instance.time = seconds;

		instance.runTaskTimerAsynchronously(plugin, 0, 20);
	}

	public static void stop() {
		instance.cancel();
	}

	@Override
	public void run() {
		ScoreboardManager.getInstance().minuteCountdown(time, BoardType.GAME);

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
