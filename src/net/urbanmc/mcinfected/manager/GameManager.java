package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
		Location spawn = null;

		switch (state) {
			case LOBBY:
				spawn = MapManager.getInstance().getLobby().getSpawn();
				break;
			case COUNTDOWN:
				spawn = MapManager.getInstance().getLobby().getSpawn();
				break;
			case INFECTION:
				spawn = MapManager.getInstance().getGameMap().getSpawn();

				ItemUtil.equipPlayer(p);
				break;
			case RUNNING:
				spawn = MapManager.getInstance().getGameMap().getSpawn();

				p.setInfected();
				ItemUtil.equipPlayer(p);
				break;
		}

		p.getOnlinePlayer().teleport(spawn);
	}

	public boolean onHumanDeath(GamePlayer died) {
		List<GamePlayer> humans = getTeam(false);
		List<GamePlayer> zombies = getTeam(true);

		int count = humans.size();

		if (count > 1)
			return false;
		else {
			endGame(true);
			return true;
		}
	}

	public void endGame(boolean zombiesWin) {

	}

	private List<GamePlayer> getTeam(boolean zombies) {
		List<GamePlayer> team = new ArrayList<>();

		for (Player player : Bukkit.getOnlinePlayers()) {
			GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

			if (p.isInfected() == zombies) {
				team.add(p);
			}
		}

		return team;
	}

	public enum GameState {
		LOBBY, COUNTDOWN, INFECTION, RUNNING
	}
}
