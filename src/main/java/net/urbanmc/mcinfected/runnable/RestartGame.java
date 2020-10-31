package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.manager.ScoreboardManager;
import net.urbanmc.mcinfected.util.VoteUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RestartGame extends BukkitRunnable {

	private MCInfected plugin;

	public RestartGame(MCInfected plugin) {
		this.plugin = plugin;
		runTaskLater(plugin, 200);
	}

	@Override
	public void run() {
		GamePlayerManager.getInstance().savePlayers();
		MapManager.getInstance().cleanseMap();

		// Set gamestate to lobby
		GameManager.getInstance().setGameState(GameManager.GameState.LOBBY);

		// Reset player states
		GameManager.getInstance().resetGame();

		// Change scoreboard
		ScoreboardManager.getInstance().changeBoardText(ScoreboardManager.BoardType.LOBBY);

		// Reset map votes
		MapManager.getInstance().resetMapVotes();

		// Reset lobby timer
		plugin.newGameStart();

		// Display maps again
		String mapDisplay = VoteUtil.getFormattedSpecific();
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(mapDisplay);
		}

		// Check if ample players
		if (Bukkit.getOnlinePlayers().size() > MCInfected.getSufficientPlayers()) {
			MCInfected.getGameStart().amplePlayers();
		}

		// Unload current map
		Bukkit.getScheduler().runTaskLater(plugin, () -> MapManager.getInstance().unloadCurrentMap(), 20L);
	}
}
