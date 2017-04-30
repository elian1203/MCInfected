package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class Message extends Command {

	public Message() {
		super("message", "command.message", false, Collections.singletonList("msg"));
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		if (args.length < 2) {
			messageSender(sender, "/" + label + " [player] [message]");
			return;
		}

		Player targetPlayer = Bukkit.getPlayer(args[0]);

		if (targetPlayer == null) {
			messageSender(sender, Messages.getInstance().getString("player_not_found"));
			return;
		}

		GamePlayer target = GamePlayerManager.getInstance().getGamePlayer(targetPlayer);

		if (p != null && target.isIgnoring(p.getUniqueId())) {
			messagePlayer(p, Messages.getInstance().getString("has_you_ignored", targetPlayer.getName()));
			return;
		}

		String message = "";

		for (int i = 1; i < args.length; i++) {
			message += args[i] + " ";
		}

		message = message.trim();

		String from = sender instanceof Player ? sender.getName() : "CONSOLE", to = targetPlayer.getName();

		if (p != null) {
			p.setLastMessenger(target);
		}

		target.setLastMessenger(p);

		messageSender(sender, Messages.getInstance().getString("message_to", to, message));
		messagePlayer(target, Messages.getInstance().getString("message_from", from, message));
	}
}
