package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.Rank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stats extends Command {

	public Stats() {
		super("stats", "command.stats", false);
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		GamePlayer tp;

		if (args.length == 0 && sender instanceof Player) {
			tp = p;
		} else if (args.length > 0) {
			tp = GamePlayerManager.getInstance().getGamePlayerByName(args[0]);
		} else {
			messageSender(sender, "/stats [player]");
			return;
		}

		if (tp == null) {
			messageSender(sender, color("&4Player not found."));
			return;
		}

		String message = build(tp);

		messageSender(sender, message);
	}

	private String build(GamePlayer p) {
		StringBuilder builder = new StringBuilder();

		Rank rank = p.getRank();

		builder.append(color("&aStats: &6" + p.getOfflinePlayer().getName()) + "\n");
		builder.append(color("&aRank: &6" + rank.getLevel() + " &a// &6" + rank.getName()) + "\n");
		builder.append(color("&aCookies: &6" + p.getCookies()) + "\n");
		builder.append(color("&aKills: &6" + p.getKills()) + "\n");
		builder.append(color("&aDeaths: &6" + p.getDeaths()) + "\n");
		builder.append(color("&aKDR: &6" + p.getKDR()) + "\n");
		builder.append(color("&aHighest KillStreak: &6" + p.getHighestKillStreak()));

		return builder.toString();
	}
}
