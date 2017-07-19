package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GameManager.GameState;
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
		GameState state = GameManager.getInstance().getGameState();

		if (state == GameState.LOBBY || state == GameState.COUNTDOWN) {
			messagePlayer(p, Messages.getInstance().getString("kit_cannot_use_yet"));
			return;
		}

		if (args.length == 0) {
			List<String> allowedKits = convertToNames(KitManager.getInstance().getKitsForPlayer(p));
			String joined = StringUtils.join(allowedKits, ", ");

			messagePlayer(p, Messages.getInstance().getString("kits_you_have", joined));

			return;
		}

		net.urbanmc.mcinfected.object.Kit kit = KitManager.getInstance().getKitByName(args[0]);

		if (kit == null) {
			messagePlayer(p, Messages.getInstance().getString("kit_nonexistent"));
			return;
		}

		if (!kit.playerHasAccess(p)) {
			messagePlayer(p, Messages.getInstance().getString("no_access_kit"));
			return;
		}

		p.setKit(kit);
		messagePlayer(p, Messages.getInstance().getString("you_selected_kit", kit.getName()));
	}

	private List<String> convertToNames(List<net.urbanmc.mcinfected.object.Kit> kits) {
		kits.forEach(kit -> System.out.println(kit.getName()));
		return kits.stream().map(net.urbanmc.mcinfected.object.Kit::getName).collect(Collectors.toList());
	}
}
