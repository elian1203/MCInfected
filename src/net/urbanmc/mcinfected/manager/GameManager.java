package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.Location;

public class GameManager {

	private static GameManager instance = new GameManager();

	private GameState state;

	public static GameManager getInstance() {
		return instance;
	}

	private GameManager() {
		state = GameState.LOBBY;
	}

	public GameState getGameState() {
		return state;
	}

	public void setGameState(GameState state) {
		this.state = state;
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

	public enum GameState {
		LOBBY, COUNTDOWN, INFECTION, RUNNING;
	}
}
