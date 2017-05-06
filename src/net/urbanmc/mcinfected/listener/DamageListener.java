package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GameManager.GameState;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.InvincibilityManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		GameState state = GameManager.getInstance().getGameState();

		if (state.equals(GameState.LOBBY) || state.equals(GameState.COUNTDOWN) || state.equals(GameState.FINISHED)) {
			e.setCancelled(true);
			return;
		}

		if (!(e.getEntity() instanceof Player))
			return;

		Player player = (Player) e.getEntity();
		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player.getUniqueId());

		if (InvincibilityManager.getInstance().in(p)) {
			e.setCancelled(true);
		}
	}
}
