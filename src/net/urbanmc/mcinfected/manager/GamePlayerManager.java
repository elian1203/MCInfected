package net.urbanmc.mcinfected.manager;

import com.google.gson.Gson;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.GamePlayerList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GamePlayerManager {

    private static GamePlayerManager instance = new GamePlayerManager();

    private final File file = new File("plugins/MCInfected", "players.json");

    private List<GamePlayer> players;

    public static GamePlayerManager getInstance() {
        return instance;
    }

    private GamePlayerManager() {
        loadFile();
        loadPlayers();
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

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public GamePlayer getGamePlayerByUniqueId(UUID uuid) {
        for (GamePlayer p : players) {
            if (p.getUniqueId().equals(uuid))
                return p;
        }

        return null;
    }

    @SuppressWarnings("deprecation")
    public GamePlayer getGamePlayerByName(String name) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(name);

        if (op == null || op.getUniqueId() == null)
            return null;

        return getGamePlayerByUniqueId(op.getUniqueId());
    }
}
