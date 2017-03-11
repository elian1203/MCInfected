package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.UUID;

public class Ignore extends Command {

	public Ignore() {
		super("ignore", "command.ignore", true, Collections.emptyList());
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		if (args.length == 0) {
			messagePlayer(p, "/ignore [player]");
			return;
		}

		Player targetPlayer = Bukkit.getPlayer(args[0]);

		if (targetPlayer == null) {
			messagePlayer(p, Messages.getInstance().getString("player_not_found"));
			return;
		}

		if (targetPlayer.hasPermission("command.ignore.exempt")) {
			messagePlayer(p, color("&4You cannot ignore this player!"));
			return;
		}

		UUID uuid = targetPlayer.getUniqueId();

		boolean ignoring = p.isIgnoring(uuid);

		if (ignoring) {
			p.removeFromIgnoring(uuid);
		} else {
			p.addToIgnoring(uuid);
		}

		messagePlayer(p, Messages.getInstance().getString("ignoring_player_" + !ignoring, targetPlayer.getName()));
	}
}
