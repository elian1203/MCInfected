package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.command.CommandSender;

public class Sneak extends Command {

    public Sneak() {
        super("sneak", "command.sneak", true);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
        if (p.isSneaking()) {
            p.setSneaking(false);
            p.getOnlinePlayer().setSneaking(false);

            messagePlayer(p, color("&6You have enabled auto-sneak!"));
        } else {
            p.setSneaking(true);
            p.getOnlinePlayer().setSneaking(true);

            messagePlayer(p, color("&6You have disabled auto-sneak!"));
        }
    }
}
