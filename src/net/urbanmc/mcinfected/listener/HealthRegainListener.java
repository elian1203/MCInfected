package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GameManager.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public class HealthRegainListener implements Listener {

	@EventHandler
	public void onEntityRegainHealth(EntityRegainHealthEvent e) {
		if (GameManager.getInstance().getGameState() != GameState.RUNNING)
			return;

		if (e.getRegainReason() != RegainReason.REGEN && e.getRegainReason() != RegainReason.SATIATED)
			return;

		if (e.getEntity() instanceof Player) {
			e.setCancelled(true);
		}
	}
}
