package net.urbanmc.mcinfected;

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;

public class Scoreboard implements Listener{

    private Objective obj;
    private static Scoreboard instance = new Scoreboard();

    public static Scoreboard getInstance() { return instance; }

    public Scoreboard () {
        createLobbyObj();
    }

    private void createLobbyObj() {
        org.bukkit.scoreboard.Scoreboard lobbyBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        obj = lobbyBoard.registerNewObjective("board" , "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.RED + "MCInfected");
        Score players = obj.getScore(ChatColor.AQUA + "Players:");
        players.setScore(10);
        obj.getScore(ChatColor.BOLD + String.valueOf(0)).setScore(9);
    }

    public void createRunningObj() {
        int[] players = sortPlayers();
        Objective running = obj.getScoreboard().registerNewObjective("running", "dummy");
        obj.unregister();
        obj = running;
        running.setDisplaySlot(DisplaySlot.SIDEBAR);
        running.getScore(ChatColor.AQUA + "Humans:").setScore(10);
        running.getScore(ChatColor.BOLD + ""  + ChatColor.AQUA + String.valueOf(players[0])).setScore(9);
        running.getScore(ChatColor.RED + "Zombies:").setScore(7);
        running.getScore(ChatColor.BOLD + ""  + ChatColor.RED + String.valueOf(players[1])).setScore(6);

        Bukkit.getOnlinePlayers().forEach((player) -> {
            player.setScoreboard(obj.getScoreboard());
        });
    }

    private void replaceEntry(Objective obj, String removeableEntry, String newEntry) {
            int score = obj.getScore(removeableEntry).getScore();
            obj.getScoreboard().resetScores(removeableEntry);
            obj.getScore(newEntry).setScore(score);
    }

    public void addPlayersToGame() {

        switch(GameManager.getInstance().getGameState()) {
            case RUNNING:
                int zombies = sortPlayers()[1];
                replaceEntry(obj,ChatColor.BOLD + "" + ChatColor.RED + String.valueOf(zombies - 1), ChatColor.BOLD + "" + ChatColor.RED + String.valueOf(zombies));
                break;
            default:
                replaceEntry(obj,ChatColor.BOLD + String.valueOf(Bukkit.getOnlinePlayers().size() - 1), ChatColor.BOLD + String.valueOf(Bukkit.getOnlinePlayers().size()));
                break;
        }


       Bukkit.getOnlinePlayers().forEach((player) -> {
           player.setScoreboard(obj.getScoreboard());
       });
    }

    public void addZombie() {
        int[] players = sortPlayers();
        replaceEntry(obj,ChatColor.BOLD + "" + ChatColor.RED + String.valueOf(players[1] - 1), ChatColor.BOLD + "" + ChatColor.RED + String.valueOf(players[1]));
        replaceEntry(obj,ChatColor.BOLD + "" + ChatColor.RED + String.valueOf(players[0] + 1), ChatColor.BOLD + "" + ChatColor.RED + String.valueOf(players[0]));

        Bukkit.getOnlinePlayers().forEach((player) -> {
            player.setScoreboard(obj.getScoreboard());
        });
    }


    public void minuteCountdown(int time) {

        try {
            obj.getScore(ChatColor.RED + "0:" + (time + 1));
            obj.getScoreboard().resetScores(ChatColor.RED + "0:" + (time +1));
        }
        catch (IllegalArgumentException e) {
            obj.getScore(" ").setScore(1);
            obj.getScore(ChatColor.RED + "0:" + time).setScore(0);
            return;
        }

        if(time == 0) {
            obj.getScoreboard().resetScores(ChatColor.RED + "0:" + (time +1));
            return;
        }

        replaceEntry(obj, ChatColor.RED + "0:" + (time + 1), ChatColor.RED + "0:" + time);
    }

    private int[] sortPlayers() {
        int humans = 0;
        int zombies = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

            if(p.isInfected()) {
                zombies +=1;
                continue;
            }

            humans += 1;
        }

        int[] players = new int[2];
        players[0] = humans;
        players [1] = zombies;
        return players;
    }



}
