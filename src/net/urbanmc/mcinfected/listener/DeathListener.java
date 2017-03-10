package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GameManager.GameState;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.text.MessageFormat;
import java.util.Random;

public class DeathListener implements Listener {
   private Random r = new Random();

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
        }
        else {

            String cause = "death";
            Entity enemy = getDamager(e);

            if (enemy instanceof Player) {
                cause = ((Player) enemy).getName();
            }

            int random = r.nextInt(3) + 1;

            String infectedMessages = (String) Messages.getInstance().getString("has_become_zombie" + random);

            String formattedDeathMessage = MessageFormat.format(infectedMessages, player.getName(), cause);

            Bukkit.broadcastMessage(formattedDeathMessage);

                p.setInfected();
        }

        ItemUtil.equipPlayer(p);

        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent e) {

        if (!GameManager.getInstance().getGameState().equals(GameState.RUNNING))
            return;

        if (!(e.getEntity() instanceof Player))
            return;

        if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)
                || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
                || e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE))
            return;

        Player player = (Player) e.getEntity();

        double health = player.getHealth() - e.getDamage();

        if (health > 0)
            return;

        GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

        p.setDeaths(p.getDeaths() + 1);

        if (p.isInfected()) {
            player.teleport(MapManager.getInstance().getGameMap().getSpawn());
        }
        else {

            int random = r.nextInt(3) + 1;

            String infectedMessages = (String) Messages.getInstance().getString("has_become_zombie" + random);

            String formattedDeathMessage = MessageFormat.format(infectedMessages, player.getName(), "Death");

            Bukkit.broadcastMessage(formattedDeathMessage);

            p.setInfected();
        }

        ItemUtil.equipPlayer(p);

        e.setCancelled(true);
    }


    private Entity getDamager(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile) {

            Projectile proj = (Projectile) e.getDamager();

            ProjectileSource projsource = proj.getShooter();

            if (!(projsource instanceof Player))
                return e.getDamager();

            return (Player) projsource;
        }

        return e.getDamager();
    }


    private String getDeathMessage() {
        return "";
    }
}
