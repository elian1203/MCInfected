package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.KillStreak;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KillStreakManager {

    private static KillStreakManager instance = new KillStreakManager();

    private List<KillStreak> killStreaks;

    public static KillStreakManager getInstance() {
        return instance;
    }

    private KillStreakManager() {
        loadKillStreaks();
    }

    private void loadKillStreaks() {
        FileConfiguration data = YamlConfiguration.loadConfiguration(new File("plugins/MCInfected", "killstreaks.yml"));
        ConfigurationSection sect = data.getConfigurationSection("killstreaks");

        for (String amount : sect.getKeys(false)) {
            boolean repeatable = sect.getBoolean(amount + ".repeatable");
            List<ItemStack> rewards = new ArrayList<ItemStack>();

            for (String item : sect.getStringList(amount + ".items")) {
                rewards.add(ItemUtil.getItem(item));
            }

            killStreaks.add(new KillStreak(Integer.parseInt(amount), repeatable, rewards));
        }
    }

    public List<KillStreak> getKillStreaks() {
        return killStreaks;
    }

    public void reloadKillStreaks() {
        killStreaks.clear();
        loadKillStreaks();
    }
}
