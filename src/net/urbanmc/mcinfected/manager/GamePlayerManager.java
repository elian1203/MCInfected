package net.urbanmc.mcinfected.manager;

import com.google.gson.Gson;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.GamePlayerList;
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
			Scanner scanner = new Scanner(file);

			players = new Gson().fromJson(scanner.nextLine(), GamePlayerList.class).getPlayers();

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
			PrintWriter writer = new PrintWriter(file);

			writer.write(new Gson().toJson(new GamePlayerList(players)));

			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public List<GamePlayer> getPlayers() {
		return players;
	}

	public void register(Player p) {
		UUID uuid = p.getUniqueId();

		if (getGamePlayer(uuid) != null)
			return;

		GamePlayer gamePlayer =
				new GamePlayer(uuid, 0, 0, 0, 0, 0, 0, 1, new ArrayList<>());

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
}
