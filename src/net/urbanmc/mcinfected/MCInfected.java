package net.urbanmc.mcinfected;

import net.urbanmc.mcinfected.listener.*;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.runnable.GameStart;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MCInfected extends JavaPlugin {

	private static GameStart gameStart;

	@Override
	public void onEnable() {
		registerListeners();
		registerGame();
	}

	@Override
	public void onDisable() {
		GamePlayerManager.getInstance().savePlayers();
	}

	private void registerListeners() {
		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvents(new AttackListener(), this);
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new CakeListener(), this);
		pm.registerEvents(new ChatListener(), this);
		pm.registerEvents(new CommandListener(), this);
		pm.registerEvents(new DamageListener(), this);
		pm.registerEvents(new DeathListener(), this);
		pm.registerEvents(new FoodConsumeListener(), this);
		pm.registerEvents(new FoodLevelListener(), this);
		pm.registerEvents(new GrenadeListener(this), this);
		pm.registerEvents(new JoinListener(), this);
		pm.registerEvents(new SneakListener(), this);
	}

	private void registerGame() {
		GameManager.getInstance().setPlugin(this);
		gameStart = new GameStart(this);
	}

	public static GameStart getGameStart() {
		return gameStart;
	}
}
