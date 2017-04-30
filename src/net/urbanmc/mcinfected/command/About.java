package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class About extends Command {

	public About() {
		super("about", "command.about", false, Arrays.asList("info", "mcinfected"));
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		String message = Messages.getInstance().getString("about");

		messageSender(sender, message);
	}
}
