package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GameManager.GameState;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DeathListener implements Listener {

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

		p.setDeaths(p.getDeaths() + 1);

		// TODO: Finish
	}
/*
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

		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

		p.setDeaths(p.getDeaths() + 1);

		if (p.isInfected()) {
			player.teleport(MapManager.getInstance().getGameMap().getSpawn());
		} else {
			p.setInfected();
			player.sendMessage(Messages.getInstance().getString("you_are_zombie"));
		}

		String deathMessage;

		Player killer = getDamagerAsPlayer(e.getDamager());

		if (killer != null) {
			Random r = new Random();

			int i = r.nextInt(3);

			deathMessage = Messages.getInstance().getString("has_become_zombie_" + i, player.getName(), killer.getName
			());
		} else {
			deathMessage = Messages.getInstance().getString("has_become_zombie_unknown", player.getName());
		}

		Bukkit.broadcastMessage(deathMessage);

		ItemUtil.equipPlayer(p);

		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDefath(EntityDamageEvent e) {
		if (!GameManager.getInstance().getGameState().equals(GameState.RUNNING))
			return;

		if (!(e.getEntity() instanceof Player))
			return;

		if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) ||
				e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) ||
				e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE))
			return;

		Player player = (Player) e.getEntity();

		double health = player.getHealth() - e.getDamage();

		if (health > 0)
			return;

		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

		p.setDeaths(p.getDeaths() + 1);

		if (p.isInfected()) {
			player.teleport(MapManager.getInstance().getGameMap().getSpawn());
		} else {
			p.setInfected();
			player.sendMessage(Messages.getInstance().getString("you_are_zombie"));
		}

		String deathMessage = Messages.getInstance().getString("has_become_zombie_unknown", player.getName());

		Bukkit.broadcastMessage(deathMessage);

		ItemUtil.equipPlayer(p);

		e.setCancelled(true);
	}
*/
}
