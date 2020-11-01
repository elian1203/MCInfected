package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.ScoreboardManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (GameManager.getInstance().getGameState() == GameManager.GameState.RUNNING) {
            GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(event.getPlayer());

            if (!gamePlayer.isInfected()) {
                // Schedule for the next tick
                Bukkit.getScheduler().runTask(MCInfected.getInstance(),
                        () -> GameManager.getInstance().onHumanDeath(gamePlayer));
            }
        }

        // Update scoreboard
        if (GameManager.getInstance().getGameState() == GameManager.GameState.LOBBY)
            ScoreboardManager.getInstance().updatePlayersOnBoard(ScoreboardManager.BoardType.LOBBY);
        else
            ScoreboardManager.getInstance().updatePlayersOnBoard(ScoreboardManager.BoardType.GAME);
    }

}
