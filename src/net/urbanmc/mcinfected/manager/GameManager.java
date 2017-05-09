package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.ScoreboardManager.BoardType;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.runnable.GameTimer;
import net.urbanmc.mcinfected.runnable.RestartServer;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameManager {

	private static GameManager instance = new GameManager();

	private MCInfected plugin;

	private GameState state;

	private GameManager() {
		state = GameState.LOBBY;
	}

	public static GameManager getInstance() {
		return instance;
	}

	public GameState getGameState() {
		return state;
	}

	public void setGameState(GameState state) {
		this.state = state;
	}

	public void setPlugin(MCInfected plugin) {
		this.plugin = plugin;
	}

	public void loadPlayer(GamePlayer p) {
		PlayerInventory inv = p.getOnlinePlayer().getInventory();

		inv.clear();
		inv.setArmorContents(null);

		Location spawn = null;
		BoardType type = null;

		switch (state) {
			case LOBBY:
				spawn = MapManager.getInstance().getLobby().getSpawn();
				type = BoardType.LOBBY;
				break;
			case COUNTDOWN:
				spawn = MapManager.getInstance().getLobby().getSpawn();
				type = BoardType.LOBBY;
				break;
			case INFECTION:
				spawn = MapManager.getInstance().getGameMap().getSpawn();
				type = BoardType.GAME;

				ItemUtil.equipPlayer(p);
				break;
			case RUNNING:
				spawn = MapManager.getInstance().getGameMap().getSpawn();
				type = BoardType.GAME;

				p.setInfected();
				ItemUtil.equipPlayer(p);
				break;
		}

		ScoreboardManager.getInstance().addPlayersToGame(type);
		p.getOnlinePlayer().teleport(spawn);
	}

	public boolean onHumanDeath(GamePlayer died) {
		List<GamePlayer> humans = getHumans();

		int count = humans.size();

		if (count > 1)
			return false;
		else {
			endGame(true, died);
			return true;
		}
	}

	public void endGame(boolean zombiesWin, GamePlayer died) {
		setGameState(GameState.FINISHED);
		GameTimer.stop();

		List<GamePlayer> humans = getHumans();

		String message;

		if (zombiesWin) {
			message = Messages.getInstance().getString("end_game_zombies_win", died.getOfflinePlayer().getName());

			GamePlayerManager.getInstance().giveAllScores(150, true);
			GamePlayerManager.getInstance().giveAllCookies(15, true);
		} else {
			String formatted = StringUtils
					.join(humans.stream().map(p -> p.getOnlinePlayer().getName()).collect(Collectors.toList()), ", ");
			message = Messages.getInstance().getString("end_game_humans_win", formatted);

			GamePlayerManager.getInstance().giveAllScores(300, false);
			GamePlayerManager.getInstance().giveAllCookies(30, false);
		}

		Bukkit.broadcastMessage(message);

		new RestartServer(plugin);
	}

	public List<GamePlayer> getHumans() {
		List<GamePlayer> team = new ArrayList<>();

		for (Player player : Bukkit.getOnlinePlayers()) {
			GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

			if (!p.isInfected()) {
				team.add(p);
			}
		}

		return team;
	}

	public enum GameState {
		LOBBY, COUNTDOWN, INFECTION, RUNNING, FINISHED
	}
}
