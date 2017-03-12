package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

public class ChatListener implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(e.getPlayer());

		String format = Messages.getInstance()
				.getString("chat_format", p.getRank().getColor().getChar(), p.getRank().getName(), "%1$s", "%2$s");

		e.setFormat(format);

		if (!e.getPlayer().hasPermission("command.ignore.exempt")) {
			ArrayList<Player> remove = new ArrayList<>();

			for (Player player : e.getRecipients()) {
				GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(player);

				if (gamePlayer.isIgnoring(p.getUniqueId())) {
					remove.add(player);
				}
			}

			e.getRecipients().removeAll(remove);
		}
	}
}
