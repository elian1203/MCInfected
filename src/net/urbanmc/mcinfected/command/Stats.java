package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.Rank;
import net.urbanmc.mcinfected.manager.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class Stats extends Command {

	public Stats() {
		super("stats", "command.stats", false, Collections.emptyList());
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		GamePlayer tp;

		if (args.length == 0 && sender instanceof Player) {
			tp = p;
		} else if (args.length > 0) {
			tp = GamePlayerManager.getInstance().getGamePlayer(args[0]);
		} else {
			messageSender(sender, "/stats [player]");
			return;
		}

		if (tp == null) {
			messageSender(sender, Messages.getInstance().getString("player_not_found"));
			return;
		}

		String message = build(tp);

		messageSender(sender, message);
	}

	private String build(GamePlayer p) {
		Rank rank = p.getRank();

		String message = Messages.getInstance().getString(
				"player_stats",
				p.getOfflinePlayer().getName(),
				rank.getLevel(),
				rank.getName(),
				p.getScores(),
				p.getCookies(),
				p.getKills(),
				p.getDeaths(),
				p.getKDR(),
				p.getHighestKillStreak());

		return message;
	}
}
