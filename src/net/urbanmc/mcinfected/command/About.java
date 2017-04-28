package net.urbanmc.mcinfected.command;

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
		String message = color("&aThis plugin was developed by Elian & Silverwolfg11. We do not take any credit " +
				                       "for any of the ideas of this plugin, only its code. Many of these ideas were" +
				                       "developed by Supertt007. This game is based off of Infected from " +
				                       "Call of Duty: Modern Warfare 3. For more information, " +
				                       "visit the game wiki at http://mcinfected.wikia.com/wiki/MCInfected_Wiki");

		messageSender(sender, message);
	}
}
