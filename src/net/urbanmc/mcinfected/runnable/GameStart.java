package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.object.Map;
import net.urbanmc.mcinfected.util.Messages;
import net.urbanmc.mcinfected.util.VoteUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GameStart implements Runnable {

	private MCInfected plugin;
	private int taskId, time;
	private Map gameMap;

	public GameStart(MCInfected plugin) {
		this.plugin = plugin;
		this.time = 240;
	}

	@Override
	public void run() {
		if (time == 240 || time == 180 || time == 120 || time == 60 || time == 30 || time == 15 || time == 3 || time
				== 2 || time == 1) {
			broadcastTime();
		}

		boolean enoughPlayers = enoughPlayers();

		if (time == 15 && !enoughPlayers) {
			insufficientPlayers();
		} else if (time == 15 && enoughPlayers) {
			mapSelection();
		}

		if (time == 0 && enoughPlayers) {
			preInfection();
		}

		this.time--;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public void sufficientPlayers() {
		time = 89;
		plugin.getServer().broadcastMessage(Messages.getInstance().getString("sufficient_players"));
	}

	private void insufficientPlayers() {
		time = 240;
		plugin.getServer().broadcastMessage(Messages.getInstance().getString("insufficient_players"));
	}

	private void broadcastTime() {
		plugin.getServer().broadcastMessage(Messages.getInstance().getString("game_starting", time));
	}

	private boolean enoughPlayers() {
		return plugin.getServer().getOnlinePlayers().size() >= 8;
	}

	private void startInfection() {
		InfectionStart task = new InfectionStart(plugin);

		int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, task, 0, 20);
		task.setTaskId(taskId);

		plugin.getServer().getScheduler().cancelTask(this.taskId);
	}

	private void mapSelection() {
		GameManager.getInstance().setGameState(GameManager.GameState.COUNTDOWN);

		gameMap = VoteUtil.getTopVotedMap();
		MapManager.getInstance().loadMap(gameMap);
		Bukkit.broadcastMessage(Messages.getInstance().getString("map_won", gameMap.getName()));
	}

	private void preInfection() {
		Location loc = gameMap.getSpawn();

		for (Player p : Bukkit.getOnlinePlayers()) {
			p.teleport(loc);
		}

		GameManager.getInstance().setGameState(GameManager.GameState.INFECTION);

		startInfection();
	}
}
