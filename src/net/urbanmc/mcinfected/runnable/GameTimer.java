package net.urbanmc.mcinfected.runnable;


import net.urbanmc.mcinfected.MCInfected;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable {

    private int time = 600;

    public GameTimer(MCInfected plugin) {
        runTaskTimerAsynchronously(plugin, 0, 20);
    }

    @Override
    public void run() {
        if (time == 0) endGame();

        time--;
    }

    public void endGame() {
        //GameEnd();
        cancel();
    }

}
