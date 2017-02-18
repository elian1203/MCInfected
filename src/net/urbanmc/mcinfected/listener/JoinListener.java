package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
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
        disableAttackCooldown(e.getPlayer());
    }


    private void disableAttackCooldown(Player p) {
        AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED);

        //At least 16 needed to disable attack cooldown
        attribute.setBaseValue(16);

        p.saveData();
    }

}
