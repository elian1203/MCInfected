package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

public class ChatListener implements Listener {

	private GroupManager gm;

	public ChatListener(GroupManager gm) {
		this.gm = gm;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();

		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

		String prefix = gm.getWorldsHolder().getWorldPermissions(player).getUserPrefix(player.getName());

		String message = "%2$s";

		if (player.hasPermission("chatcolor.red")) {
			message = ChatColor.RED + message;
		} else if (player.hasPermission("chatcolor.green")) {
			message = ChatColor.GREEN + message;
		} else if (player.hasPermission("chatcolor.pink")) {
			message = ChatColor.LIGHT_PURPLE + message;
		}

		String format;

		if (prefix.equals("")) {
			format = Messages.getInstance().getString(
					"chat_format",
					p.getRank().getColor().getChar(),
					p.getRank().getName(),
					player.getCustomName(),
					message);
		} else {
			format = Messages.getInstance().getString(
					"chat_format_prefix",
					prefix,
					p.getRank().getColor().getChar(),
					p.getRank().getName(),
					player.getCustomName(),
					message);
		}

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
