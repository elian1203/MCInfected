package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.GamePlayer;

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

	public void loadPlayer(GamePlayer p, boolean lateJoin) {

	}

	public enum GameState {
		LOBBY, COUNTDOWN, INFECTION, RUNNING;
	}
}
