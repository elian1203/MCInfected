package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Infected extends Command {

    public Infected() {
        super("infected", "command.infected", true);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
        if (GameManager.getInstance().getGameState() != GameManager.GameState.RUNNING) {
            messagePlayer(p, color("&cThe game hasn't started yet!"));
            return;
        }

        if (args.length > 1) {
            messagePlayer(p, color("&cUsage: /infected [playername]"));
            return;
        }

        if (args.length == 0) {
            String infected = p.isInfected() ? "You are infected!" : "You are not infected!";
            messagePlayer(p, color("&6" + infected));
            return;
        }

        if (args.length == 1) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);

            if (targetPlayer == null) {
                messagePlayer(p, color("&cPlayer not found."));
                return;
            }

            GamePlayer tp = GamePlayerManager.getInstance().getGamePlayerByUniqueId(targetPlayer.getUniqueId());

            String infected = targetPlayer.getName() + "is" + (tp.isInfected() ? "infected. Run!!" : "not infected.");

            messagePlayer(p, color("&4" + infected));
        }
    }
}
