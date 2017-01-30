package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class About extends Command {

	public About() {
		super("about", "command.about", false);
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		String message = ChatColor.AQUA + "This message was developed by Elian & Silverwolfg11. We do not take any" +
				" credit for any of the ideas of this plugin, only its code. Any of these ideas were developed by " +
				"Supertt007. This game is based off Infected from Call of Duty: Modern Warefare 3. For more " +
				"information, visit the game wiki at http://mcinfected.wikia.com/wiki/MCInfected_Wiki";

		messageSender(sender, message);
	}
}
