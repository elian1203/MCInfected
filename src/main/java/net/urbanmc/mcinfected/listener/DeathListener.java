package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.*;
import net.urbanmc.mcinfected.manager.GameManager.GameState;
import net.urbanmc.mcinfected.manager.ScoreboardManager.BoardType;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.util.ItemUtil;
import net.urbanmc.mcinfected.util.KillStreakUtil;
import net.urbanmc.mcinfected.util.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Random;

public class DeathListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDeath(EntityDamageEvent e) {
		GameState state = GameManager.getInstance().getGameState();

		if (!state.equals(GameState.INFECTION) && !state.equals(GameState.RUNNING))
			return;

		if (!(e.getEntity() instanceof Player))
			return;

		Player player = (Player) e.getEntity();

		double health = player.getHealth() - e.getDamage();

		if (health > 0)
			return;

		e.setCancelled(true);

		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

		player.setFallDistance(0);
		player.setHealth(20);

		if (state.equals(GameState.INFECTION)) {
			player.teleport(MapManager.getInstance().getGameMap().getSpawn());
			return;
		}

		InvincibilityManager.getInstance().add(p);

		p.setDeaths(p.getDeaths() + 1);

		String deathMessage;

		boolean entityAttack = isEntityCause(p, e.getCause());

		if (entityAttack) {
			GamePlayer attacker = p.getLastAttacker();

			attacker.setKills(attacker.getKills() + 1);
			KillStreakUtil.giveNextKillStreak(attacker);

			deathMessage = getDeathMessage(p, attacker, e.getCause());
		} else
			deathMessage = getDeathMessage(p, null, e.getCause());


		Bukkit.broadcastMessage(deathMessage);

		if (entityAttack)
			giveProperYield(p.getLastAttacker(), p, p.isInfected());

		if (p.isInfected()) {
			player.setFallDistance(0);
			player.teleport(MapManager.getInstance().getGameMap().getSpawn());
		} else if (!GameManager.getInstance().onHumanDeath(p)) {
			p.setInfected();
			p.setKillStreak(0);

			PacketUtil.removePlayersFromList(p.getOnlinePlayer());

			player.sendMessage(Messages.getInstance().getString("you_are_zombie"));

			if(e.getCause().equals(DamageCause.VOID)) {
				player.setFallDistance(0);
				player.teleport(MapManager.getInstance().getGameMap().getSpawn());
			}
		}


		ItemUtil.equipPlayer(p);

		p.setLastAttacker(null);

		ScoreboardManager.getInstance().updatePlayersOnBoard(BoardType.GAME);
	}

	private boolean isEntityCause(GamePlayer p, DamageCause cause) {
		return cause.equals(DamageCause.ENTITY_ATTACK) || cause.equals(DamageCause.ENTITY_SWEEP_ATTACK) ||
				cause.equals(DamageCause.PROJECTILE) || cause.equals(DamageCause.CUSTOM) ||
				(p.getLastAttacker() != null && cause.equals(DamageCause.VOID));
	}

	private String getDeathMessage(GamePlayer killed, GamePlayer attacker, DamageCause cause) {
		String killedName = killed.getOnlinePlayer().getName();

		if (attacker == null) {
			if (killed.isInfected()) {
				if (cause.equals(DamageCause.VOID))
					return Messages.getInstance().getString("zombie_fell", killedName);
				return Messages.getInstance().getString("zombie_died", killedName);
			} else {
				if (cause.equals(DamageCause.VOID))
					return Messages.getInstance().getString("human_fell", killedName);
				return Messages.getInstance().getString("has_become_zombie_unknown", killedName);
			}
		} else {
			String attackerName = attacker.getOnlinePlayer().getName();

			Random r = new Random();

			int i = r.nextInt(3) + 1;

			if (killed.isInfected()) {
				if(cause.equals(DamageCause.VOID))
					return Messages.getInstance().getString("human_pushed_zombie", killedName, attackerName);
				return Messages.getInstance().getString("human_killed_zombie_" + i, killedName, attackerName);
			}
			else {
				if(cause.equals(DamageCause.VOID))
					return Messages.getInstance().getString("zombie_pushed_human", killedName, attackerName);
				return Messages.getInstance().getString("zombie_killed_human_" + i, killedName, attackerName);
			}
		}
	}

	private void giveProperYield(GamePlayer attacker, GamePlayer killed, boolean zombieKilled) {
		if (zombieKilled) {
			attacker.giveScores(20);
			attacker.giveCookies(2);
		} else {
			attacker.giveScores(10);
			attacker.giveCookies(1);

			if (GameManager.getInstance().getHumans().size() > 1) {
				GamePlayerManager.getInstance().giveAllScores(20, false, killed);
				GamePlayerManager.getInstance().giveAllCookies(2, false, killed);

				GamePlayerManager.getInstance().sendBarAllTeam(Messages.getInstance().getString("human_killed_humans"),
				                                               false,
				                                               killed);
			}

			GamePlayerManager.getInstance().giveAllScores(10, true, killed);
			GamePlayerManager.getInstance().giveAllCookies(1, true, killed);

			GamePlayerManager.getInstance()
					.sendBarAllTeam(Messages.getInstance().getString("human_killed_zombies"), true);
		}
	}
}
