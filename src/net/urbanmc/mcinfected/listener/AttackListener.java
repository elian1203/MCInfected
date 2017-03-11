package net.urbanmc.mcinfected.listener;


import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class AttackListener implements Listener {

	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;

		Player player = (Player) e.getEntity();
		Player damager = getDamagerAsPlayer(e.getEntity());

		if (damager == null)
			return;

		GamePlayer attacked = GamePlayerManager.getInstance().getGamePlayer(player);
		GamePlayer attacker = GamePlayerManager.getInstance().getGamePlayer(damager);

		if (attacked.isInfected() && attacker.isInfected() || !attacked.isInfected() && !attacker.isInfected()) {
			e.setCancelled(true);
			return;
		}

		attacked.setLastAttacker(attacker);
	}

	private Player getDamagerAsPlayer(Entity entity) {
		if (entity instanceof Player)
			return (Player) entity;

		if (entity instanceof Projectile) {
			Projectile projectile = (Projectile) entity;
			ProjectileSource shooter = projectile.getShooter();

			if (shooter instanceof Player)
				return (Player) shooter;
		}

		return null;
	}
}
