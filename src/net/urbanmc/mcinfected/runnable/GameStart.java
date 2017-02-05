package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.object.Map;
import net.urbanmc.mcinfected.util.VoteUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GameStart implements Runnable {

    private MCInfected plugin;
    private int taskId, time;
    private Map gameMap;

    public GameStart(MCInfected plugin) {
        this.plugin = plugin;
        this.time = 240;
    }

    @Override
    public void run() {
        time--;

        if (time == 240 || time == 180 || time == 120 || time == 60 || time == 30 || time == 15 || time == 3 || time
                == 2 || time == 1) {
            broadcastTime();
        }

        boolean enoughPlayers = enoughPlayers();

        if (time == 25 && !enoughPlayers) {
            insufficientPlayers();
            return;
        }

        if (time == 15 && enoughPlayers)
            mapSelection();

        if (time == 0 && enoughPlayers) {
            preInfection();
            return;
        }



        if (!(time <= 0))
            return;


    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void sufficientPlayers() {
        time = 89;
        plugin.getServer().broadcastMessage(ChatColor.AQUA + "Enough players have joined the game, reducing time to " +
                "90 seconds.");
    }

    private void insufficientPlayers() {
        time = 241;
        plugin.getServer().broadcastMessage(ChatColor.AQUA + "Not enough players to play. Resetting time to 240 " +
                "seconds.");
    }

    private void broadcastTime() {
        plugin.getServer().broadcastMessage(ChatColor.AQUA + "Game starting in " + time + " seconds.");
    }

    private boolean enoughPlayers() {
        return plugin.getServer().getOnlinePlayers().size() >= 8;
    }

    private void startInfection() {
        InfectionStart task = new InfectionStart(plugin);

        int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, task, 0, 20);
        task.setTaskId(taskId);

        plugin.getServer().getScheduler().cancelTask(this.taskId);
    }

    private void mapSelection() {
        GameManager.getInstance().setGameState(GameManager.GameState.COUNTDOWN);

        Map map = VoteUtil.getTopVotedMap();
        MapManager.getInstance().loadMap(map);
        Bukkit.broadcastMessage(ChatColor.BLUE + "Map: " + map.getName());
    }

    private void preInfection() {
        Location loc = gameMap.getSpawn();

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.teleport(loc);
        }

        GameManager.getInstance().setGameState(GameManager.GameState.INFECTION);

        startInfection();
    }
}
