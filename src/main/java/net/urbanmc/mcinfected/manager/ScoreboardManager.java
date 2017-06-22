package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager implements Listener {

	private static ScoreboardManager instance = new ScoreboardManager();

	private Objective obj;
	private int time;

	private ScoreboardManager() {
		createLobbyObj();
	}

	public static ScoreboardManager getInstance() {
		return instance;
	}

	private void createLobbyObj() {
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = board.registerNewObjective("board", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.RED + "MCInfected");

		updateBoard(BoardType.LOBBY);
		applyBoard();
	}

	void addPlayersToGame(BoardType type) {
		updateBoard(type);
		applyBoard();
	}

	public void updateBoard(BoardType type) {
		String formattedTime = formatTime(time);

		String text = null;

		switch (type) {
			case LOBBY:
				text = Messages.getInstance().getString("lobby_board", Bukkit.getOnlinePlayers().size(),
				                                        formattedTime);
				break;
			case GAME:
				int[] players = sortPlayers();
				String mapName = MapManager.getInstance().getGameMap().getName();

				text = Messages.getInstance().getString("game_board", players[0], players[1], mapName, formattedTime);
				break;
		}

		resetScores();

		String[] split = text.split("\\r?\\n");

		int lineNumber = 0, score = split.length, spaces = 0;

		while (lineNumber < split.length) {
			StringBuilder line = new StringBuilder(split[lineNumber++]);

			if (line.toString().equals("")) {
				for (int i = 0; i < spaces; i++) {
					line.append(" ");
				}

				spaces++;
			}

			obj.getScore(line.toString()).setScore(score--);
		}
	}


	public void minuteCountdown(int time, BoardType type) {
		this.time = time;
		updateBoard(type);
	}

	private int[] sortPlayers() {
		int humans = 0;
		int zombies = 0;

		for (Player player : Bukkit.getOnlinePlayers()) {
			GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

			if (p.isInfected())
				zombies += 1;
			else
				humans += 1;
		}

		return new int[]{humans, zombies};
	}

	private void applyBoard() {
		Bukkit.getOnlinePlayers().forEach(player -> player.setScoreboard(obj.getScoreboard()));
	}

	private String formatTime(long seconds) {
		return String.format("%d:%02d", seconds / 60, seconds % 60);
	}

	public void amplePlayers(int time) {
		String formatted = formatTime(time);
		obj.getScoreboard().resetScores(formatted);
	}

	public void insufficientPlayers() {
		obj.getScoreboard().resetScores("0:15");
	}

	private void resetScores() {
		for (String entry : obj.getScoreboard().getEntries()) {
			obj.getScoreboard().resetScores(entry);
		}
	}

	public enum BoardType {
		LOBBY, GAME
	}
}
