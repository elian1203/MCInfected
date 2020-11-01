package net.urbanmc.mcinfected.runnable;


import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.manager.ScoreboardManager;
import net.urbanmc.mcinfected.util.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class GameTimer implements Runnable {

	private static final GameTimer instance = new GameTimer();

	private BukkitTask task = null;
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

		// Check that previous tasks are stopped
		stop();

		instance.task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, instance, 0, 20);
	}

	public static void stop() {
		if (instance.task != null && !instance.task.isCancelled())
			instance.task.cancel();
	}

	@Override
	public void run() {
		ScoreboardManager.getInstance().minuteCountdown(time);

		if (time == 300 || time == 60)
			broadcastTime();

		if (time == 0) {
			GameManager.getInstance().endGame(false, null);
			stop();
			return;
		}

		time--;

		// Do this after decrement so no items are given immediately
		checkHumanSurvival();
	}

	private void checkHumanSurvival() {
		if (time > 0 && (time % 60 == 0)) {
			GameManager.getInstance().onTeam(true, (p, gp) -> {
				p.getInventory().addItem(new ItemStack(Material.MELON, 1));
				PacketUtil.sendActionBar(p, Messages.getInstance().getString("human_survived_minute"), "white");
			});
		}
	}

	private void broadcastTime() {
		Bukkit.broadcastMessage(Messages.getInstance().getString("time_remaining", time / 60));
	}
}
