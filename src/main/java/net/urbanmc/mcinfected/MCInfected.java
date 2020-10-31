package net.urbanmc.mcinfected;

import net.urbanmc.mcinfected.listener.*;
import net.urbanmc.mcinfected.runnable.GameStart;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MCInfected extends JavaPlugin {

	private static MCInfected instance;

	private static GameStart gameStart;

	public static GameStart getGameStart() {
		return gameStart;
	}

	public static int getSufficientPlayers() {
		return 6;
	}

	public static MCInfected getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		registerListeners();
		newGameStart();
	}

	private void registerListeners() {
		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvents(new ArmorListener(), this);
		pm.registerEvents(new AttackListener(), this);
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new CakeListener(), this);
		pm.registerEvents(new ChatListener(), this);
		pm.registerEvents(new CommandListener(), this);
		pm.registerEvents(new CreatureSpawn(), this);
		pm.registerEvents(new DamageListener(), this);
		pm.registerEvents(new DeathListener(), this);
		pm.registerEvents(new FoodConsumeListener(), this);
		pm.registerEvents(new FoodLevelListener(), this);
		pm.registerEvents(new GrenadeListener(), this);
		pm.registerEvents(new HealthRegainListener(), this);
		pm.registerEvents(new ItemDropListener(), this);
		pm.registerEvents(new JoinListener(), this);
		pm.registerEvents(new ShopListener(), this);
		pm.registerEvents(new SneakListener(), this);
	}

	private void registerGame() {
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		newGameStart();
	}

	public void newGameStart() {
		gameStart = new GameStart(this);
	}

}
