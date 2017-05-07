package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.ScoreboardManager;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.manager.ScoreboardManager.BoardType;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.Map;
import net.urbanmc.mcinfected.util.ItemUtil;
import net.urbanmc.mcinfected.util.VoteUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStart extends BukkitRunnable {

	private MCInfected plugin;
	private int time;
	private Map gameMap;

	public GameStart(MCInfected plugin) {
		this.plugin = plugin;
		this.time = 240;

		runTaskTimer(plugin, 0, 20);
	}

	@Override
	public void run() {
		if (time == 240 || time == 180 || time == 120 || time == 60 || time == 30 || time == 15 || time == 3 ||
				time == 2 || time == 1) {
			broadcastTime();
		}

		boolean enoughPlayers = enoughPlayers();

		ScoreboardManager.getInstance().minuteCountdown(time, BoardType.LOBBY);

		if (time == 15 && !enoughPlayers) {
			insufficientPlayers();
		} else if (time == 15 && enoughPlayers) {
			mapSelection();
		}

		if (time == 0 && enoughPlayers) {
			preInfection();
		}

		time--;
	}

	public int getTime() {
		return time;
	}

	public void amplePlayers() {
		ScoreboardManager.getInstance().amplePlayers(time + 1);

		time = 90;
		Bukkit.broadcastMessage(Messages.getInstance().getString("sufficient_players"));
	}

	private void insufficientPlayers() {
		ScoreboardManager.getInstance().insufficientPlayers();

		time = 240;
		Bukkit.broadcastMessage(Messages.getInstance().getString("insufficient_players"));
	}

	private void broadcastTime() {
		Bukkit.broadcastMessage(Messages.getInstance().getString("game_starting", time));
	}

	private boolean enoughPlayers() {
		return plugin.getServer().getOnlinePlayers().size() >= MCInfected.getSufficientPlayers(); // Normally 8
	}

	private void startInfection() {
		new InfectionStart(plugin);
		cancel();
	}

	private void mapSelection() {
		GameManager.getInstance().setGameState(GameManager.GameState.COUNTDOWN);

		gameMap = VoteUtil.getTopVotedMap();

		Bukkit.broadcastMessage(Messages.getInstance().getString("map_won", gameMap.getName()));

		MapManager.getInstance().loadMap(gameMap);
		MapManager.getInstance().setGameMap(gameMap);
	}

	private void preInfection() {
		Location loc = gameMap.getSpawn();

		for (Player p : Bukkit.getOnlinePlayers()) {
			p.teleport(loc);

			GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(p);
			gamePlayer.setGamesPlayed(gamePlayer.getGamesPlayed() + 1);

			ItemUtil.equipPlayer(gamePlayer);
		}

		GameManager.getInstance().setGameState(GameManager.GameState.INFECTION);

		Bukkit.broadcastMessage(Messages.getInstance().getString("infection_start"));

		startInfection();
	}
}
