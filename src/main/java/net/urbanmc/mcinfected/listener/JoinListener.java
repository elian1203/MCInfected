package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.runnable.GameStart;
import net.urbanmc.mcinfected.util.PacketUtil;
import net.urbanmc.mcinfected.util.VoteUtil;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JoinListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		GamePlayerManager.getInstance().register(player);

		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

		GameManager.getInstance().loadPlayer(p);
		disableAttackCooldown(player);

		player.setHealth(20);
		player.setFoodLevel(20);

		GameStart gameStart = MCInfected.getGameStart();

		if (gameStart.getTime() > 90 && Bukkit.getOnlinePlayers().size() >= MCInfected.getSufficientPlayers()) {
			gameStart.amplePlayers();
		}

		e.setJoinMessage(getJoinMessage(e.getPlayer().getName()));

		displayMaps(player);
	}

	private void disableAttackCooldown(Player p) {
		AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
		attribute.setBaseValue(20);

		p.saveData();
	}

	private String getJoinMessage(String playerName) {

		if (GameManager.getInstance().getGameState().equals(GameManager.GameState.RUNNING)) {

			addZombieToRunning(Bukkit.getPlayer(playerName));

			return Messages.getInstance().getString("joined_running", playerName);
		} else
			return Messages.getInstance().getString("joined_pregame",
			                                        playerName,
			                                        Bukkit.getOnlinePlayers().size(),
			                                        MCInfected.getSufficientPlayers());
	}

	private void displayMaps(Player p) {
		if (GameManager.getInstance().getGameState() != GameManager.GameState.LOBBY)
			return;

		Bukkit.getScheduler()
				.runTaskLater(MCInfected.getInstance(), () -> p.sendMessage(VoteUtil.getFormattedSpecific()), 20);
	}

	private void addZombieToRunning(Player p) {
		PacketUtil.removePlayersFromList(p);

		List<Player> infected = new ArrayList<>();
		GamePlayer tempPlayer;

		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player.equals(p))
				continue;

			tempPlayer = GamePlayerManager.getInstance().getGamePlayer(p);

			if(tempPlayer.isInfected())
				infected.add(player);
		}

		PacketUtil.removePlayersFromPlayerList(infected, p);
	}
}
