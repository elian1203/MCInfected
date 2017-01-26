package net.urbanmc.mcinfected.manager;

import com.google.gson.Gson;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.GamePlayerList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

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

            players = new ArrayList<GamePlayer>();
        }
    }
}
