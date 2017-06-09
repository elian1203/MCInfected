package net.urbanmc.mcinfected.command;


import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.*;

public class ForceStart extends Command{


    public ForceStart(String name, String permission, boolean onlyPlayer, java.util.List<String> aliases) {
        super("forcestart", "command.forcestart", false, Collections.singletonList("fs"));
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
        if(!GameManager.getInstance().getGameState().equals(GameManager.GameState.LOBBY)) {
            messageSender(sender,Messages.getInstance().getString("game_started"));
            return;
        }

        Bukkit.broadcastMessage(Messages.getInstance().getString("game_forcestarted"));
        MCInfected.getGameStart().mapSelection();
    }
}
