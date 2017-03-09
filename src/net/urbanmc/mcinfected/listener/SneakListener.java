package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SneakListener implements Listener {

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(e.getPlayer());

		if (p.isSneaking()) {
			e.setCancelled(true);
		}
	}
}
