package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class ScoreboardManager implements Listener {

	private static ScoreboardManager instance = new ScoreboardManager();
	private Objective obj;

	private ScoreboardManager() {
		createLobbyObj();
	}

	public static ScoreboardManager getInstance() {
		return instance;
	}

	private void createLobbyObj() {
		org.bukkit.scoreboard.Scoreboard lobbyBoard = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = lobbyBoard.registerNewObjective("board", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.RED + "MCInfected");
		obj.getScore(ChatColor.AQUA + "Players:").setScore(10);
		obj.getScore(ChatColor.BOLD + String.valueOf(0)).setScore(9);
	}

	public void createRunningObj() {
		int[] players = sortPlayers();

		Objective running = obj.getScoreboard().registerNewObjective("running", "dummy");
		running.setDisplayName(obj.getDisplayName());
		obj.unregister();
		obj = running;

		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		obj.getScore(ChatColor.AQUA + "Humans:").setScore(10);
		obj.getScore(color("&b&l" + players[0])).setScore(9);

		obj.getScore(" ").setScore(8);

		obj.getScore(ChatColor.RED + "Zombies:").setScore(7);
		obj.getScore(color("&c&l" + players[1])).setScore(6);

		obj.getScore("  ").setScore(1);
		obj.getScore(color("&bMap:&f " + MapManager.getInstance().getGameMap().getName())).setScore(0);

		applyBoard();
	}

	private void replaceEntry(String removeableEntry, String newEntry) {
		int score = obj.getScore(removeableEntry).getScore();
		obj.getScoreboard().resetScores(removeableEntry);
		obj.getScore(newEntry).setScore(score);
	}

	public void addPlayersToGame() {

		switch (GameManager.getInstance().getGameState()) {
			case RUNNING:
				int zombies = sortPlayers()[1];
				replaceEntry(color("&c&l" + (zombies - 1)), color("&c&l" + zombies));
				break;
			default:
				replaceEntry(
						ChatColor.BOLD + String.valueOf(Bukkit.getOnlinePlayers().size() - 1),
						ChatColor.BOLD + String.valueOf(Bukkit.getOnlinePlayers().size()));
				break;
		}


		applyBoard();
	}

	public void addZombie() {
		int[] players = sortPlayers();

		//Zombies
		replaceEntry(color("&c&l" + (players[1] - 1)), color("&c&l" + (players[1])));
		//Humans
		replaceEntry(color("&b&l" + (players[0] + 1)), color("&b&l" + (players[0])));

		applyBoard();
	}


	public void minuteCountdown(int time) {
		if (time == 0) {
			obj.getScoreboard().resetScores("0:01");
		} else {
			String oldFormatted = formatTime(time + 1);
			String formatted = formatTime(time);

			replaceEntry(oldFormatted, formatted);
		}
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

	private String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	private void applyBoard() {
		Bukkit.getOnlinePlayers().forEach((player) -> {
			player.setScoreboard(obj.getScoreboard());
		});
	}

	private String formatTime(long seconds) {
		return String.format("%d:%02d", seconds / 60, seconds % 60);
	}
}
