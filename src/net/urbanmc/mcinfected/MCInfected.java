package net.urbanmc.mcinfected;

import net.urbanmc.mcinfected.runnable.GameStart;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MCInfected extends JavaPlugin {

    private GameStart runnable;

    @Override
    public void onEnable() {
        createFiles();
        registerGame();
    }

    private void createFiles() {
        getDataFolder().mkdir();

        File file;

        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                file = new File(getDataFolder(), "killstreaks.yml");
            } else if (i == 1) {
                file = new File(getDataFolder(), "kits.yml");
            } else if (i == 2) {
                file = new File(getDataFolder(), "maps.yml");
            } else {
                file = new File(getDataFolder(), "ranks.yml");
            }

            if (!file.exists()) {
                saveResource(file.getName(), false);
            }
        }
    }

    private void registerGame() {
        runnable = new GameStart(this);

        int taskId = getServer().getScheduler().scheduleSyncRepeatingTask(this, runnable, 0, 20);
        runnable.setTaskId(taskId);
    }
}
