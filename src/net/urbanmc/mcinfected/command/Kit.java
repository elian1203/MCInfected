package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.KitManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Kit extends Command {

	public Kit() {
		super("kit", "command.kit", true, Collections.singletonList("k"));
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		if (!p.isInfected()) {
			messagePlayer(p, Messages.getInstance().getString("must_be_infected"));
			return;
		}

		if (args.length == 0) {
			List<String> allowedKits = convertToNames(KitManager.getInstance().getKitsForPlayer(p));

			String joined = StringUtils.join(allowedKits, ", ");
		}
	}

	private List<String> convertToNames(List<net.urbanmc.mcinfected.object.Kit> kits) {
		return kits.stream().map(kit -> kit.getName()).collect(Collectors.toList());
	}
}
