package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class Shop extends Command {

	public Shop() {
		super("shop", "command.shop", true, Arrays.asList("s", "buy"));
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {

	}
}


