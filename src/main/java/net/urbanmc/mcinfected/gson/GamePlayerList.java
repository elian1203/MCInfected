package net.urbanmc.mcinfected.gson;

import net.urbanmc.mcinfected.object.GamePlayer;

import java.util.List;

public class GamePlayerList {

    private List<GamePlayer> players;

    public GamePlayerList(List<GamePlayer> players) {
        this.players = players;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }
}
