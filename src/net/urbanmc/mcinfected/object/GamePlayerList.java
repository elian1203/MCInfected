package net.urbanmc.mcinfected.object;

import java.util.List;

public class GamePlayerList {
//His
    private List<GamePlayer> players;

    public GamePlayerList(List<GamePlayer> players) {
        this.players = players;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }
}
