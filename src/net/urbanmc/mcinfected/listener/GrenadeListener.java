package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.grenade.Grenade;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GrenadeListener implements Listener {

	private MCInfected plugin;

	public GrenadeListener(MCInfected plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onGrenadeThrow(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		ItemStack is = e.getItem();

		if (is == null) {
			return;
		}

		if (!Grenade.isGrenade(is)) {
			return;
		}

		if (!GameManager.getInstance().getGameState().equals(GameManager.GameState.RUNNING))
			return;


		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(e.getPlayer());
		e.setCancelled(true);
		ItemUtil.throwItem(p, is, plugin);
	}
}
