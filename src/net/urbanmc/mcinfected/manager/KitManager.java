package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.Kit;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KitManager {

    private static KitManager instance = new KitManager();

    private FileConfiguration data;
    private List<Kit> kits;
    private Kit zombie, mother;

    private KitManager() {
        data = YamlConfiguration.loadConfiguration(new File("plugins/MCInfected", "kits.yml"));

        loadMotherAndZombie();
        loadKits();
    }

    private void loadKits() {
        kits = new ArrayList<>();

        ConfigurationSection sect = data.getConfigurationSection("kits");

        for (String name : sect.getKeys(false))
            kits.add(formKit(name));

    }

    private void loadMotherAndZombie() {

        mother = formKit("mother");
        zombie = formKit("zombie");
    }

    private Kit formKit(String path) {
        EntityType tempdisguise = null;

        if (data.contains(path + ".disguise"))
            tempdisguise = EntityType.valueOf(data.getString(path + ".disguise"));

       Kit kit = new Kit(
                data.getString(path + ".name"),
                data.getString(path + ".permission"),
                ItemUtil.getItemList(data.getStringList(path + ".armor")),
                ItemUtil.getItemList(data.getStringList(path + ".items")),
                tempdisguise
        );

        return kit;
    }


}
