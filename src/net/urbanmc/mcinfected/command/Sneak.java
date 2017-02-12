package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.util.Messages;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class Sneak extends Command {

	public Sneak() {
		super("sneak", "command.sneak", true, Collections.emptyList());
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		boolean sneaking = p.isSneaking();

		p.setSneaking(!sneaking);
		p.getOnlinePlayer().setSneaking(!sneaking);

		messagePlayer(p, Messages.getInstance().getString("sneak_" + !sneaking));
	}
}
