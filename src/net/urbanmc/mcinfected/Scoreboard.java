package net.urbanmc.mcinfected;

import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.HashMap;

public class Scoreboard implements Listener {

    private Objective obj;
    private static Scoreboard instance = new Scoreboard();
    private HashMap<Integer, String> minuteList = new HashMap<>();

    public static Scoreboard getInstance() {
        return instance;
    }

    private Scoreboard() {
        createLobbyObj();
        setMinuteList();
    }

    private void createLobbyObj() {
        org.bukkit.scoreboard.Scoreboard lobbyBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        obj = lobbyBoard.registerNewObjective("board", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.RED + "MCInfected");
        Score players = obj.getScore(ChatColor.AQUA + "Players:");
        players.setScore(10);
        obj.getScore(ChatColor.BOLD + String.valueOf(0)).setScore(9);
    }

    private void setMinuteList() {

        for (int i = 0; i < 61; i++) {
            minuteList.put(i, color("&c0:" + i));

            if (i == 60) minuteList.replace(i, color("&c1:00"));

            if (i < 10) minuteList.replace(i, color("&c0:0" + i));
        }

    }

    public void createRunningObj() {
        int[] players = sortPlayers();

        Objective running = obj.getScoreboard().registerNewObjective("running", "dummy");
        running.setDisplayName(obj.getDisplayName());
        obj.unregister();
        obj = running;

        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        obj.getScore(ChatColor.AQUA + "Humans:").setScore(10);
        obj.getScore(color("&b&l" + players[0])).setScore(9);

        obj.getScore(" ").setScore(8);

        obj.getScore(ChatColor.RED + "Zombies:").setScore(7);
        obj.getScore(color("&c&l" + players[0])).setScore(6);

        obj.getScore(" ").setScore(1);
        obj.getScore(color("&bMap:&f " + MapManager.getInstance().getGameMap().getName())).setScore(0);

        applyBoard();
    }

    private void replaceEntry(String removeableEntry, String newEntry) {
        int score = obj.getScore(removeableEntry).getScore();
        obj.getScoreboard().resetScores(removeableEntry);
        obj.getScore(newEntry).setScore(score);
    }

    public void addPlayersToGame() {

        switch (GameManager.getInstance().getGameState()) {
            case RUNNING:
                int zombies = sortPlayers()[1];
                replaceEntry(color("&c&l" + (zombies - 1)), color("&c&l" + zombies));
                break;
            default:
                replaceEntry(ChatColor.BOLD + String.valueOf(Bukkit.getOnlinePlayers().size() - 1), ChatColor.BOLD + String.valueOf(Bukkit.getOnlinePlayers().size()));
                break;
        }


        applyBoard();
    }

    public void addZombie() {
        int[] players = sortPlayers();

        //Zombies
        replaceEntry(color("&c&l" + (players[1] - 1)), color("&c&l" + (players[1])));
        //Humans
        replaceEntry(color("&b&l" + (players[0] + 1)), color("&b&l" + (players[0])));

        applyBoard();
    }


    public void minuteCountdown(int time) {
        /*
        try {
            obj.getScore(ChatColor.RED + "0:" + (time + 1));
            obj.getScoreboard().resetScores(ChatColor.RED + "0:" + (time + 1));
        } catch (IllegalArgumentException e) {
            obj.getScore(" ").setScore(1);
            obj.getScore(ChatColor.RED + "0:" + time).setScore(0);
            return;
        } */

        if (time > 61) return;

        if (time == 60) {
            obj.getScore(" ").setScore(1);
            obj.getScore(minuteList.get(60)).setScore(0);
            return;
        }

        if (time == 0) {
            obj.getScoreboard().resetScores(minuteList.get(1));
            return;
        }

        replaceEntry(minuteList.get(time + 1), minuteList.get(time));
    }

    private int[] sortPlayers() {
        int humans = 0;
        int zombies = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

            if (p.isInfected())
                zombies += 1;

            else
                humans += 1;
        }

        int[] players = new int[2];
        players[0] = humans;
        players[1] = zombies;
        return players;
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private void applyBoard() {
        Bukkit.getOnlinePlayers().forEach((player) -> {
            player.setScoreboard(obj.getScoreboard());
        });
    }

}
