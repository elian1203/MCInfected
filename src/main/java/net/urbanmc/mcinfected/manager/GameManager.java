package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.ScoreboardManager.BoardType;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.runnable.GameTimer;
import net.urbanmc.mcinfected.runnable.RestartGame;
import net.urbanmc.mcinfected.util.DisguiseUtil;
import net.urbanmc.mcinfected.util.ItemUtil;
import net.urbanmc.mcinfected.util.PacketUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameManager {

	private static GameManager instance = new GameManager();

	private MCInfected plugin = MCInfected.getInstance();

	private GameState state;
	// Track players for reset
	private Collection<UUID> playersJoined = new HashSet<>();

	private GameManager() {
		state = GameState.LOBBY;
	}

	public static GameManager getInstance() {
		return instance;
	}

	public GameState getGameState() {
		return state;
	}

	public void setGameState(GameState state) {
		this.state = state;
	}

	public void setPlugin(MCInfected plugin) {
		this.plugin = plugin;
	}

	public void loadPlayer(GamePlayer p) {
		PlayerInventory inv = p.getOnlinePlayer().getInventory();

		inv.clear();
		inv.setArmorContents(null);

		Location spawn = null;
		BoardType type = null;

		switch (state) {
			case LOBBY:
				spawn = MapManager.getInstance().getLobby().getSpawn();
				type = BoardType.LOBBY;
				break;
			case COUNTDOWN:
				spawn = MapManager.getInstance().getLobby().getSpawn();
				type = BoardType.LOBBY;
				break;
			case INFECTION:
				spawn = MapManager.getInstance().getGameMap().getSpawn();
				type = BoardType.GAME;

				ItemUtil.equipPlayer(p);
				break;
			case RUNNING:
				spawn = MapManager.getInstance().getGameMap().getSpawn();
				type = BoardType.GAME;

				p.setInfected();
				ItemUtil.equipPlayer(p);

				Bukkit.getServer().getScheduler().runTaskLater(MCInfected.getInstance(),
				                                               () -> PacketUtil
						                                               .removePlayersFromList(p.getOnlinePlayer()), 2);
				break;
		}

		playersJoined.add(p.getUniqueId());

		GamePlayerManager.getInstance().setColoredName(p);

		ScoreboardManager.getInstance().addPlayersToGame(type);
		p.getOnlinePlayer().teleport(spawn);
	}

	public boolean onHumanDeath(GamePlayer died) {
		List<GamePlayer> humans = getHumans();

		int count = humans.size();

		if (count > 1)
			return false;
		else {
			endGame(true, died);
			return true;
		}
	}

	public void endGame(boolean zombiesWin, GamePlayer died) {
		setGameState(GameState.FINISHED);
		GameTimer.stop();

		List<GamePlayer> humans = getHumans();

		String message;

		if (zombiesWin) {
			message = Messages.getInstance().getString("end_game_zombies_win", died.getOfflinePlayer().getName());

			GamePlayerManager.getInstance().giveAllScores(150, true);
			GamePlayerManager.getInstance().giveAllCookies(15, true);
		} else {
			String formatted = StringUtils
					.join(humans.stream().map(p -> p.getOnlinePlayer().getName()).collect(Collectors.toList()), ", ");
			message = Messages.getInstance().getString("end_game_humans_win", formatted);

			GamePlayerManager.getInstance().giveAllScores(300, false);
			GamePlayerManager.getInstance().giveAllCookies(30, false);
		}

		Bukkit.broadcastMessage(message);

		new RestartGame(plugin);
	}

	public void resetGame() {
		// Handles resetting all player-related things
		Location spawn = MapManager.getInstance().getLobby().getSpawn();;

		List<Player> infectedPlayers = new ArrayList<>();

		for (Player player : Bukkit.getOnlinePlayers()) {
			playersJoined.remove(player.getUniqueId());

			// Clear armor
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);

			GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(player);

			if (gamePlayer.isInfected()) {
				infectedPlayers.add(player);
				// Undisguise infected players
				DisguiseUtil.undisguisePlayer(player);
			}

			gamePlayer.resetGame();

			// Teleport to spawn
			if (!player.teleport(spawn)) {
				Bukkit.getLogger().warning("[MCInfected] Unable to teleport " + player.getName() + " to spawn. Kicking...");
				player.kickPlayer("Could not teleport to lobby!");
			}
		}

		// Reset gameplayer state for players who left and didn't come back.
		for (UUID playerID : playersJoined) {
			GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(playerID);
			gamePlayer.resetGame();
		}
		playersJoined.clear();

		// Add infected players back to player list
		PacketUtil.addPlayersToPlayerList(infectedPlayers, null);
	}

	public void onTeam(boolean humans, BiConsumer<Player, GamePlayer> playerConsumer) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

			if ((!humans && p.isInfected()) || (humans && !p.isInfected())) {
				playerConsumer.accept(player, p);
			}
		}
	}

	private List<GamePlayer> getPlayersOnSide(final boolean humans) {
		final List<GamePlayer> team = new ArrayList<>();

		onTeam(humans, (p, gp) -> {
			team.add(gp);
		});

		return team;
	}

	public List<GamePlayer> getHumans() {
		return getPlayersOnSide(true);
	}

	public List<GamePlayer> getInfected() {
		return getPlayersOnSide(false);
	}

	public enum GameState {
		LOBBY, COUNTDOWN, INFECTION, RUNNING, FINISHED
	}
}
