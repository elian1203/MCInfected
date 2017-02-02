package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.CommandManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandListener implements Listener {

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		if (CommandManager.getInstance().onExecute(e.getPlayer(), e.getMessage().substring(1))) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onServerCommand(ServerCommandEvent e) {
		if (CommandManager.getInstance().onExecute(e.getSender(), e.getCommand())) {
			e.setCancelled(true);
		}
	}
}
