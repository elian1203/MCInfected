package net.urbanmc.mcinfected.command;

import net.md_5.bungee.api.ChatColor;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.object.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Error: not player");
			return true;
		}

		Player p = (Player) sender;

		if (!GameManager.getInstance().getGameState().equals(GameManager.GameState.LOBBY)) {
			p.sendMessage(ChatColor.RED + "The map has already been decided!");
			return true;
		}

		if (args.length != 1) {
			p.sendMessage(ChatColor.GOLD + "Maps you can vote for:\n");
			String mapList = MapManager.getInstance().getMapNames().toString().replace("[", "").replace("]", "");
			p.sendMessage(ChatColor.GOLD + mapList);
		}
		

		Map map = MapManager.getInstance().getMapByName(args[0]);
		map.setVotes(map.getVotes() + 1);
		p.sendMessage(ChatColor.GOLD + "You have voted for " + map.getName() + "!");
		return true;
	}
}
