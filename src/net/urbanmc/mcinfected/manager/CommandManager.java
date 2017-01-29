package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.command.Vote;
import net.urbanmc.mcinfected.object.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

	private static CommandManager instance = new CommandManager();

	private List<Command> commands;

	public static CommandManager getInstance() {
		return instance;
	}

	private CommandManager() {
		loadCommands();
	}

	private void loadCommands() {
		commands = new ArrayList<>();

		commands.add(new Vote());
	}

	public void onExecute(CommandSender sender, String message) {
		
	}
}
