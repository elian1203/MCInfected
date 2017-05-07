package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class Help extends Command {

	public Help() {
		super("help", "command.help", false, Collections.emptyList());
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		String message = Messages.getInstance().getString("help");

		messageSender(sender, message);
	}
}
