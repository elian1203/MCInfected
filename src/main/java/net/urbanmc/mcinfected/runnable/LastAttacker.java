package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class LastAttacker extends BukkitRunnable{

    private GamePlayer player;

    public LastAttacker(GamePlayer player) {
        this.player = player;

        runTaskLater(MCInfected.getInstance(), 600);
    }

    @Override
    public void run() {
        player.setLastAttacker(null);
        player.setLastAttackerRunnable(null);
    }


}
