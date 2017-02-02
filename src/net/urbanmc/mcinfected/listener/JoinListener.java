package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		GamePlayerManager.getInstance().register(e.getPlayer());

		GamePlayer p = GamePlayerManager.getInstance().getGamePlayerByUniqueId(e.getPlayer().getUniqueId());

		GameManager.getInstance().loadPlayer(p, GameManager.getInstance().getGameState() == GameManager.GameState
				.RUNNING);
	}
}
