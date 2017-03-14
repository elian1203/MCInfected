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

		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(e.getPlayer());

		GameManager.getInstance().loadPlayer(p);
		disableAttackCooldown(e.getPlayer());
	}

	private void disableAttackCooldown(Player p) {
		AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
		attribute.setBaseValue(20);

		p.saveData();
	}
}
