package net.urbanmc.mcinfected;

import net.urbanmc.mcinfected.listener.CommandListener;
import net.urbanmc.mcinfected.listener.DeathListener;
import net.urbanmc.mcinfected.listener.JoinListener;
import net.urbanmc.mcinfected.listener.SneakListener;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.runnable.GameStart;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MCInfected extends JavaPlugin {

	private GameStart runnable;

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

	private void registerListeners() {
		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvents(new CommandListener(), this);
		pm.registerEvents(new DeathListener(), this);
		pm.registerEvents(new JoinListener(), this);
		pm.registerEvents(new SneakListener(), this);
	}

	private void registerGame() {
		runnable = new GameStart(this);

		int taskId = getServer().getScheduler().scheduleSyncRepeatingTask(this, runnable, 0, 20);
		runnable.setTaskId(taskId);
	}
}
