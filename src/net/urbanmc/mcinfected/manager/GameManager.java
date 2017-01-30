package net.urbanmc.mcinfected.manager;

public class GameManager {

	private static GameManager instance = new GameManager();

	private GameState state = GameState.LOBBY;

	public static GameManager getInstance() {
		return instance;
	}

	private GameManager() {

	}

	public GameState getGameState() {
		return state;
	}

	public void setGameState(GameState state) {
		this.state = state;
	}

	public enum GameState {
		LOBBY, COUNTDOWN, INFECTION, RUNNING;
	}
}
