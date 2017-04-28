package net.urbanmc.mcinfected.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.urbanmc.mcinfected.gson.GamePlayerList;
import net.urbanmc.mcinfected.gson.GamePlayerListSerializer;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GamePlayerManager {

	private static GamePlayerManager instance = new GamePlayerManager();

	private final File file = new File("plugins/MCInfected", "players.json");

	private List<GamePlayer> players;

	private GamePlayerManager() {
		loadFile();
		loadPlayers();
	}

	public static GamePlayerManager getInstance() {
		return instance;
	}

	private void loadFile() {
		if (!file.getParentFile().isDirectory()) {
			file.getParentFile().mkdir();
		}

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void loadPlayers() {
		try {
			Gson gson = new GsonBuilder().registerTypeAdapter(GamePlayerList.class, new GamePlayerListSerializer())
					.create();

			Scanner scanner = new Scanner(file);

			players = gson.fromJson(scanner.nextLine(), GamePlayerList.class).getPlayers();

			scanner.close();
		} catch (Exception ex) {
			if (!(ex instanceof NoSuchElementException)) {
				ex.printStackTrace();
			}

			players = new ArrayList<>();
		}
	}

	public void savePlayers() {
		try {
			Gson gson = new GsonBuilder().registerTypeAdapter(GamePlayerList.class, new GamePlayerListSerializer())
					.create();

			PrintWriter writer = new PrintWriter(file);

			writer.write(gson.toJson(new GamePlayerList(players)));

			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void register(Player p) {
		UUID uuid = p.getUniqueId();

		if (getGamePlayer(uuid) != null)
			return;

		GamePlayer gamePlayer =
				new GamePlayer(uuid, 0, 0, 0, 0, 0, 0, RankManager.getInstance().getRankByLevel(1), new ArrayList<>());

		players.add(gamePlayer);

		savePlayers();
	}

	public GamePlayer getGamePlayer(UUID uuid) {
		for (GamePlayer p : players) {
			if (p.getUniqueId().equals(uuid))
				return p;
		}

		return null;
	}

	@SuppressWarnings("deprecation")
	public GamePlayer getGamePlayer(String name) {
		OfflinePlayer op = Bukkit.getOfflinePlayer(name);

		if (op == null || op.getUniqueId() == null)
			return null;

		return getGamePlayer(op.getUniqueId());
	}

	public GamePlayer getGamePlayer(Player player) {
		return getGamePlayer(player.getUniqueId());
	}

	public void giveAllScores(long scores, boolean zombies, GamePlayer... exclude) {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();

		if (exclude != null) {
			for (GamePlayer p : exclude) {
				players.remove(p.getOnlinePlayer());
			}
		}

		for (Player player : players) {
			GamePlayer p = getGamePlayer(player);

			if (p.isInfected() == zombies) {
				p.giveScores(scores);
			}
		}
	}

	public void giveAllCookies(long cookies, boolean zombies, GamePlayer... exclude) {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();

		if (exclude != null) {
			for (GamePlayer p : exclude) {
				players.remove(p.getOnlinePlayer());
			}
		}

		for (Player player : players) {
			GamePlayer p = getGamePlayer(player);

			if (p.isInfected() == zombies) {
				p.giveCookies(cookies);
			}
		}
	}

	public void messageAllTeam(String message, boolean zombies) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			GamePlayer p = getGamePlayer(player);

			if (p.isInfected() == zombies) {
				p.getOnlinePlayer().sendMessage(message);
			}
		}
	}
}
