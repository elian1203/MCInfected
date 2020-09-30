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
		Player player = e.getPlayer();

		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

		String message = "%2$s";

		String format = Messages.getInstance().getString(
				"chat_format",
				p.getRank().getColor().getChar(),
				p.getRank().getName(),
				player.getCustomName(),
				message);

		e.setFormat(format);

		if (!e.getPlayer().hasPermission("command.ignore.exempt")) {
			ArrayList<Player> remove = new ArrayList<>();

			for (Player recipient : e.getRecipients()) {
				GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(recipient);

				if (gamePlayer.isIgnoring(p.getUniqueId())) {
					remove.add(recipient);
				}
			}

			e.getRecipients().removeAll(remove);
		}
	}
}
