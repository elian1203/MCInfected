package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.string.SingleInputString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager implements Listener {

	private static ScoreboardManager instance = new ScoreboardManager();

	private BoardType currentType = null;

	private Objective obj;
	private int time;

	// String Replacements
	private SingleInputString playerCounterStr = SingleInputString.EMPTY;
	private SingleInputString numInfectedStr = SingleInputString.EMPTY;
	private SingleInputString numHumanStr = SingleInputString.EMPTY;
	private SingleInputString timeStr = SingleInputString.EMPTY;

	private ScoreboardManager() {
		createLobbyObj();
	}

	public static ScoreboardManager getInstance() {
		return instance;
	}

	private void createLobbyObj() {
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = board.registerNewObjective("MCInfected", "board", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.RED + "MCInfected");

		// Create teams for no-flicker
		Team playerCounter = board.registerNewTeam("playerCounter");
		playerCounter.addEntry(ChatColor.BLACK + "" + ChatColor.WHITE);

		Team numInfected = board.registerNewTeam("numInfected");
		numInfected.addEntry(ChatColor.RED + "" + ChatColor.WHITE);

		Team numHuman = board.registerNewTeam("numHuman");
		numHuman.addEntry(ChatColor.BLUE + "" + ChatColor.WHITE);

		Team timeEntry = board.registerNewTeam("fTime");
		timeEntry.addEntry(ChatColor.GOLD + "" + ChatColor.WHITE);

		changeBoardText(BoardType.LOBBY);
		applyBoard();
	}

	public void changeBoardText(BoardType type) {
		if (currentType != null && currentType == type)
			return;

		currentType = type;

		String text = null;

		switch (type) {
			case LOBBY:
				text = Messages.getInstance().getString("lobby_board");
				break;
			case GAME:
				String mapName = MapManager.getInstance().getGameMap().getName();

				text = Messages.getInstance().getString("game_board", mapName);
				break;
		}

		resetScores();

		String[] split = text.split("\\r?\\n");

		int lineNumber = 0,
			score = split.length,
			spaces = 0;

		while (lineNumber < split.length) {
			String line = split[lineNumber++];

			if (line.isEmpty()) {
				StringBuilder spaceBuilder = new StringBuilder();
				for (int i = 0; i < spaces; i++) {
					spaceBuilder.append(" ");
				}

				spaces++;
				line = spaceBuilder.toString();
			}
			else if(line.contains("%players%")) {
				String[] pCountSplit = line.split("%players%");
				playerCounterStr = new SingleInputString(pCountSplit[0],
										pCountSplit.length == 2 ? pCountSplit[1] : "");
				// Set to team entry
				line = ChatColor.BLACK + "" + ChatColor.WHITE;
			}
			else if (line.contains("%numinfected%")) {
				String[] infectedSplit = line.split("%numinfected%");
				numInfectedStr = new SingleInputString(infectedSplit[0],
						infectedSplit.length == 2 ? infectedSplit[1] : "");
				line = ChatColor.RED + "" + ChatColor.WHITE;
			}
			else if (line.contains("%numhuman%")) {
				String[] humanSplit = line.split("%numhuman%");
				numHumanStr = new SingleInputString(humanSplit[0],
						humanSplit.length == 2 ? humanSplit[1] : "");
				line = ChatColor.BLUE + "" + ChatColor.WHITE;
			}
			else if (line.contains("%time%")) {
				String[] timeSplit = line.split("%time%");
				timeStr = new SingleInputString(timeSplit[0],
						timeSplit.length == 2 ? timeSplit[1] : "");
				line = ChatColor.GOLD + "" + ChatColor.WHITE;
			}

			updatePlayersOnBoard(type);
			updateTime();

			obj.getScore(line).setScore(score--);
		}
	}


	public void updatePlayersOnBoard(BoardType type) {
		Scoreboard scoreboard = obj.getScoreboard();
		if (type == BoardType.GAME) {
			int[] players = sortPlayers();
			scoreboard.getTeam("numHuman").setPrefix(numHumanStr.build(players[0]));
			scoreboard.getTeam("numInfected").setPrefix(numInfectedStr.build(players[1]));
		}
		else {
			scoreboard.getTeam("playerCounter")
					.setPrefix(playerCounterStr.build(Bukkit.getOnlinePlayers().size()));
		}
	}

	void addPlayersToGame(BoardType type) {
		updatePlayersOnBoard(type);
		applyBoard();
	}

	public void minuteCountdown(int time) {
		this.time = time;
		updateTime();
	}

	private void updateTime() {
		final Scoreboard scoreboard = obj.getScoreboard();
		String formattedTime = formatTime(time);
		scoreboard.getTeam("fTime").setPrefix(timeStr.build(formattedTime));
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

	private void resetScores() {
		for (String entry : obj.getScoreboard().getEntries()) {
			obj.getScoreboard().resetScores(entry);
		}
	}

	public enum BoardType {
		LOBBY, GAME
	}
}
