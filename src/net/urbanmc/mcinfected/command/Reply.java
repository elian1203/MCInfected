package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class Reply extends Command {

	public Reply() {
		super("reply", "command.reply", true, Collections.singletonList("r"));
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		if (args.length == 0) {
			messageSender(sender, "/r [message]");
			return;
		}

		GamePlayer target = p.getLastMessager();

		if (target == null) {
			messageSender(sender, Messages.getInstance().getString("player_not_found"));
			return;
		}

		if (target.isIgnoring(p.getUniqueId())) {
			messagePlayer(p, Messages.getInstance().getString("has_you_ignored", target.getOnlinePlayer().getName()));
			return;
		}

		String message = StringUtils.join(args, " ");

		String from = p.getOnlinePlayer().getName(), to = target.getOnlinePlayer().getName();

		target.setLastMessager(p);

		messagePlayer(p, Messages.getInstance().getString("message_to", to, message));
		messagePlayer(target, Messages.getInstance().getString("message_from", from, message));
	}
}
