package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.anjocaido.groupmanager.events.GMUserEvent;
import org.anjocaido.groupmanager.events.GMUserEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PermissionsListener implements Listener {

	@EventHandler
	public void onPermissionsUpdate(GMUserEvent e) {
		if (e.getAction() == Action.USER_SUBGROUP_CHANGED || e.getAction() == Action.USER_PERMISSIONS_CHANGED) {
			Player bukkitPlayer = Bukkit.getPlayer(e.getUserName());
			GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(bukkitPlayer);

			GamePlayerManager.getInstance().setColoredName(p);
		}
	}
}
