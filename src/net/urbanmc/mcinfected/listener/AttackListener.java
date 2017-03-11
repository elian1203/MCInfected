package net.urbanmc.mcinfected.listener;


import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackListener implements Listener{

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {


        if(!(e.getEntity() instanceof Player))
            return;


        Player player = (Player) e.getEntity();

        double health = player.getHealth() - e.getDamage();

        if (health < 0)
            return;

        if(!(e.getDamager() instanceof Player)) {
            getDamager(e, player);
            return;
        }

        Player enemy = (Player) e.getDamager();

        GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);
        GamePlayer ene = GamePlayerManager.getInstance().getGamePlayer(enemy);

        if(p.isInfected() && ene.isInfected())
        {
            e.setCancelled(true);
            return;
        }

        if(!p.isInfected() && !ene.isInfected()) {
            e.setCancelled(true);
            return;
        }

        p.setLastAttacker(ene);
    }


    public void getDamager(EntityDamageByEntityEvent e, Player player) {

        if(!(e.getDamager() instanceof Projectile))
            return;

        if(!(((Projectile) e.getDamager()).getShooter() instanceof Player))
            return;

        GamePlayer ene = GamePlayerManager.getInstance().getGamePlayer(
                (Player)((Projectile) e.getDamager()).getShooter());

        GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);


        if(p.isInfected() && ene.isInfected())
        {
            e.setCancelled(true);
            return;
        }

        if(!p.isInfected() && !ene.isInfected()) {
            e.setCancelled(true);
            return;
        }

        p.setLastAttacker(ene);
    }

}
