package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;

public class Help extends Command {

    public Help() {
        super("help", "command.help", false, Collections.emptyList());
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
        String message = color(
                "&2==**== McInfected ==**==" +
                "\n &6/about&f: A little about the plugin" +
                "\n &6/help&f: &fMakes this command appear" +
                "\n &6/ignore (player)&f: Ignore that one player!" +
                "\n &6/infected (player)&f: Find out if a player is infected!" +
                "\n &6/message (player)&f: Send a nice little message to your buddy!" +
                "\n &6/reply&f: Reply to your buddy!" +
                "\n &6/shop&f: Access the amazing shop!" +
                "\n &6/sneak&f: Toggle sneak!" +
                "\n &6/stats&f: Check out your amazing player stats!" +
                "\n &6/vote&f: Vote for your favorite map!");

                messageSender(sender, message);
    }
}
