package net.urbanmc.mcinfected;

import net.urbanmc.mcinfected.listener.*;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.runnable.GameStart;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MCInfected extends JavaPlugin {

	@Override
	public void onEnable() {
		createFiles();
		registerListeners();
		registerGame();
	}

	@Override
	public void onDisable() {
		GamePlayerManager.getInstance().savePlayers();
	}

	private void createFiles() {
		getDataFolder().mkdir();

		for (int i = 0; i < 4; i++) {
			File file;

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

	private void registerListeners() {
		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvents(new AttackListener(), this);
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new ChatListener(), this);
		pm.registerEvents(new CommandListener(), this);
		pm.registerEvents(new DeathListener(), this);
		pm.registerEvents(new FoodConsumeListener(), this);
		pm.registerEvents(new FoodLevelListener(), this);
		pm.registerEvents(new GrenadeListener(this), this);
		pm.registerEvents(new JoinListener(), this);
		pm.registerEvents(new SneakListener(), this);
	}

	private void registerGame() {
		MapManager.getInstance().setLobbyVars();

		GameStart runnable = new GameStart(this);

		int taskId = getServer().getScheduler().scheduleSyncRepeatingTask(this, runnable, 0, 20);
		runnable.setTaskId(taskId);
	}
}
