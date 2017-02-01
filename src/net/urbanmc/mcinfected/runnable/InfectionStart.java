package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.MCInfected;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class InfectionStart implements Runnable {

    private MCInfected plugin;
    private int taskId, time;

    public InfectionStart(MCInfected plugin) {
        this.plugin = plugin;
        this.time = 30;
    }

    @Override
    public void run() {

    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    private void broadcastTime() {
        Bukkit.broadcastMessage(ChatColor.AQUA + "Infection coming in " + time + " seconds.");
    }

    private void stop() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
