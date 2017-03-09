package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GameManager.GameState;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DeathListener implements Listener {

	@EventHandler
	public void onPlayerDeathByEntity(EntityDamageByEntityEvent e) {
		if (!GameManager.getInstance().getGameState().equals(GameState.RUNNING))
			return;

		if (!(e.getEntity() instanceof Player))
			return;

		Player player = (Player) e.getEntity();

		double health = player.getHealth() - e.getDamage();

		if (health > 0)
			return;


	}

	@EventHandler
	public void onPlayerDeath(EntityDamageEvent e) {
		if (!GameManager.getInstance().getGameState().equals(GameState.RUNNING))
			return;

		if (!(e.getEntity() instanceof Player))
			return;

		Player player = (Player) e.getEntity();

		double health = player.getHealth() - e.getDamage();

		if (health > 0)
			return;

		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

		if (p.isInfected()) {
			player.teleport(MapManager.getInstance().getGameMap().getSpawn());
		} else {
			p.setInfected();
		}

		ItemUtil.equipPlayer(p);
	}

	private String getDeathMessage() {
		return "";
	}
}
