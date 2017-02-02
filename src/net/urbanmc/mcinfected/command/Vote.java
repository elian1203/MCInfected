package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.Map;
import net.urbanmc.mcinfected.util.VoteUtil;
import org.bukkit.command.CommandSender;

public class Vote extends Command {

    public Vote() {
        super("vote", "command.vote", true);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
        if (!GameManager.getInstance().getGameState().equals(GameManager.GameState.LOBBY)) {
            messagePlayer(p, color("&cThe map has already been decided."));
            return;
        }

        if (p.hasVoted()) {
            messagePlayer(p, color("&5You have already voted for a map."));
            return;
        }

        if (args.length == 0) {
            messagePlayer(p, color("&5Maps you can vote for:\n" + VoteUtil.getFormattedSpecific()));
            return;
        }

        Map map = MapManager.getInstance().getSpecificByName(args[0]);

        if (map == null) {
            messagePlayer(p, color("&4Map not found."));
        }

        VoteUtil.addVotes(map, 1);

        p.setVoted(true);

        messagePlayer(p, color("&6You have voted for " + map.getName() + "."));
    }
}
