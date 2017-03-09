package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.command.*;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

		commands.add(new About());
		commands.add(new Infected());
		commands.add(new Shop());
		commands.add(new Sneak());
		commands.add(new Stats());
		commands.add(new Vote());
	}

	public List<Command> getCommands() {
		return commands;
	}

	public Command getCommandByName(String name) {
		for (Command command : commands) {
			if (command.getName().equalsIgnoreCase(name))
				return command;
		}

		return null;
	}

	public boolean onExecute(CommandSender sender, String message) {
		String label = message.split(" ")[0];

		Command command = getCommandByName(label);

		if (command == null)
			return false;

		if (command.isOnlyPlayer() && !(sender instanceof Player)) {
			sender.sendMessage("Error: not player");
			return true;
		}

		if (!command.getPermission().equals("") && !sender.hasPermission(command.getPermission())) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
			return true;
		}

		String subbed = message.substring(label.length()).trim();
		String[] args = subbed.length() == 0 ? new String[0] : subbed.split(" ");

		GamePlayer p = null;

		if (sender instanceof Player) {
			p = GamePlayerManager.getInstance().getGamePlayer((Player) sender);
		}

		command.execute(sender, label, args, p);

		return true;
	}
}
