package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.MCInfected;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class GameStart implements Runnable {

	private MCInfected plugin;
	private int taskId, time;

	public GameStart(MCInfected plugin) {
		this.plugin = plugin;
		this.time = 240;
	}

	@Override
	public void run() {
		time--;

		if (time == 240 || time == 180 || time == 120 || time == 60 || time == 30 || time == 15 || time == 3 || time
				== 2 || time == 1) {
			broadcastTime();
		}

		boolean enoughPlayers = enoughPlayers();

		if (time == 25 && !enoughPlayers) {
			insufficientPlayers();
			return;
		}

		if (!(time <= -1))
			return;


	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public void sufficientPlayers() {
		time = 89;
		Bukkit.broadcastMessage(ChatColor.AQUA + "Enough players have joined the game, reducing time to 90 seconds.");
	}

	private void insufficientPlayers() {
		time = 241;
		Bukkit.broadcastMessage(ChatColor.AQUA + "Not enough players to play. Resetting time to 240 seconds.");
	}

	private void broadcastTime() {
		Bukkit.broadcastMessage(ChatColor.AQUA + "Game starting in " + time + " seconds.");
	}

	private boolean enoughPlayers() {
		return Bukkit.getOnlinePlayers().size() >= 8;
	}

	private void startInfection() {
		InfectionStart task = new InfectionStart(plugin);

		int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, 0, 20);
		task.setTaskId(taskId);

		Bukkit.getScheduler().cancelTask(this.taskId);
	}
}
