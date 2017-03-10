package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(e.getPlayer());

		String format = Messages.getInstance().getString("chat_format", p.getRank().getName(), "%1$s", "%2$s");

		e.setFormat(format);
	}
}
