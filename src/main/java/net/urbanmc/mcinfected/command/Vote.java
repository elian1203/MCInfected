package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.Map;
import net.urbanmc.mcinfected.util.VoteUtil;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class Vote extends Command {

	public Vote() {
		super("vote", "command.vote", true, Collections.singletonList("v"));
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		if (!GameManager.getInstance().getGameState().equals(GameManager.GameState.LOBBY)) {
			messagePlayer(p, color("&cThe map has already been decided."));
			return;
		}

		if (args.length == 0) {
			messagePlayer(p, VoteUtil.getFormattedSpecific());
			return;
		}

		if (p.hasVoted()) {
			messagePlayer(p, color("&5You have already voted for a map."));
			return;
		}

		Map map;

		if (isInt(args[0])) {
			map = MapManager.getInstance().getSpecificByIndex(Integer.parseInt(args[0]) - 1);
		} else {
			map = MapManager.getInstance().getSpecificByName(args[0]);
		}

		if (map == null) {
			messagePlayer(p, color("&4Map not found."));
			return;
		}

		VoteUtil.addVotes(p, map, 1);
	}

	private boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
}
