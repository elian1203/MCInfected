package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.List;

public class VoteStart extends Command{

    private List<UUID> voteList = new ArrayList<>();
    private int minPlayersToVoteStart = 6;

    public VoteStart() {
        super("votestart", "command.votestart", true, Collections.singletonList("vs"));
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {

        if (!GameManager.getInstance().getGameState().equals(GameManager.GameState.LOBBY)) {
            messageSender(sender, Messages.getInstance().getString("game_started"));
            return;
        }

        //Subject to change
        if(Bukkit.getOnlinePlayers().size() < minPlayersToVoteStart) {
            messagePlayer(p, Messages.getInstance().getString("votestart_not_enough_players", minPlayersToVoteStart));
            return;
        }

        if(Bukkit.getOnlinePlayers().size() >= MCInfected.getSufficientPlayers()) {
            messagePlayer(p, Messages.getInstance().getString("votestart_enough_players"));
            return;
        }

        if(voteList.contains(p.getUniqueId())) {
            messagePlayer(p, Messages.getInstance().getString("already_voted_start"));
            return;
        }

        if(voteList.size() == minPlayersToVoteStart - 1) {
            Bukkit.broadcastMessage(Messages.getInstance().getString("game_votestarted"));
            MCInfected.getGameStart().forceStart();
            return;
        }

        voteList.add(p.getUniqueId());

        Bukkit.broadcastMessage(Messages.getInstance().getString("player_voted_start", p.getOnlinePlayer().getName(), voteList.size(), minPlayersToVoteStart));

    }
}
