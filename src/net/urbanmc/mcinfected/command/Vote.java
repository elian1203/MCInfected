package net.urbanmc.mcinfected.command;

import net.md_5.bungee.api.ChatColor;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.Map;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class Vote extends Command {

	public Vote() {
		super("vote", "command.vote", true);
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		if (!GameManager.getInstance().getGameState().equals(GameManager.GameState.LOBBY)) {
			messagePlayer(p, ChatColor.RED + "The map has already been decided!");
			return;
		}

		if (args.length == 0) {
			String mapList = StringUtils.join(MapManager.getInstance().getMapNames(), ", ");

			messagePlayer(p, ChatColor.GOLD + "Maps you can vote for:\n");
			messagePlayer(p, ChatColor.GOLD + mapList);

			return;
		}

		Map map = MapManager.getInstance().getMapByName(args[0]);
		map.setVotes(map.getVotes() + 1);

		messagePlayer(p, ChatColor.GOLD + "You have voted for " + map.getName() + "!");
	}
}
