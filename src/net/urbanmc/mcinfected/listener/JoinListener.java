package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.runnable.GameStart;
import net.urbanmc.mcinfected.util.VoteUtil;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		GamePlayerManager.getInstance().register(e.getPlayer());

		GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(e.getPlayer());

		GameManager.getInstance().loadPlayer(p);
		disableAttackCooldown(e.getPlayer());

		GameStart gameStart = MCInfected.getGameStart();

		if (gameStart.getTime() > 90 && Bukkit.getOnlinePlayers().size() >= 1 /* Normally 12 */) {
			gameStart.amplePlayers();
		}

		e.setJoinMessage(getJoinMessage(e.getPlayer().getName()));

		displayMaps(e.getPlayer());
	}

	private void disableAttackCooldown(Player p) {
		AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
		attribute.setBaseValue(20);

		p.saveData();
	}

	private String getJoinMessage(String playerName) {

		if (GameManager.getInstance().getGameState().equals(GameManager.GameState.RUNNING))
			return Messages.getInstance().getString("joined_running", playerName);

		else
			return Messages.getInstance().getString("joined_pregame",
			                                        playerName,
			                                        Bukkit.getOnlinePlayers().size(),
			                                        MCInfected.getSufficientPlayers());
	}

	private void displayMaps(Player p) {

		if(GameManager.getInstance().getGameState() != GameManager.GameState.LOBBY) return;
		p.sendMessage(VoteUtil.getFormattedSpecific());

	}
}
