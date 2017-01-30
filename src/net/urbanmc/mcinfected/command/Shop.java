package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.command.CommandSender;

public class Shop extends Command {

    public Shop() {
        super("shop", "command.shop", true);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {

    }
}


