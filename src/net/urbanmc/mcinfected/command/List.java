package net.urbanmc.mcinfected.command;

import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GameManager.GameState;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.Command;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.stream.Collectors;

public class List extends Command {

	public List() {
		super("list", "command.list", false, Collections.emptyList());
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args, GamePlayer p) {
		GameState state = GameManager.getInstance().getGameState();

		String message;

		if (state != GameState.RUNNING) {
			java.util.List<String> names =
					Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());

			message = Messages.getInstance().getString("list_lobby", StringUtils.join(names, ", "));
		} else {
			GamePlayerManager manager = GamePlayerManager.getInstance();

			java.util.List<String> humans =
					Bukkit.getOnlinePlayers().stream().map(manager::getGamePlayer).filter(gp -> !gp.isInfected())
							.map(gp -> gp.getOnlinePlayer().getName()).collect(Collectors.toList());

			java.util.List<String> zombies =
					Bukkit.getOnlinePlayers().stream().map(manager::getGamePlayer).filter(GamePlayer::isInfected)
							.map(gp -> gp.getOnlinePlayer().getName()).collect(Collectors.toList());

			String humansJoined = StringUtils.join(humans, ", "), zombiesJoined = StringUtils.join(zombies, ", ");

			message = Messages.getInstance().getString("list_game", humansJoined, zombiesJoined);
		}

		messageSender(sender, message);
	}
}
