package net.urbanmc.mcinfected;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class MCInfected extends JavaPlugin {

    @Override
    public void onEnable() {
        createFiles();
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
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
