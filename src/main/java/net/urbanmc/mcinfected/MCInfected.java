package net.urbanmc.mcinfected;

import net.urbanmc.mcinfected.listener.*;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.runnable.GameStart;
import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MCInfected extends JavaPlugin {

	private static GameStart gameStart;

	public static GameStart getGameStart() {
		return gameStart;
	}

	public static int getSufficientPlayers() {
		return 8;
	}

	@Override
	public void onEnable() {
		registerListeners();
		registerGame();
	}

	private void registerListeners() {
		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvents(new ArmorListener(), this);
		pm.registerEvents(new AttackListener(), this);
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new CakeListener(), this);
		pm.registerEvents(new ChatListener(getPlugin(GroupManager.class)), this);
		pm.registerEvents(new CommandListener(), this);
		pm.registerEvents(new CreatureSpawn(), this);
		pm.registerEvents(new DamageListener(), this);
		pm.registerEvents(new DeathListener(), this);
		pm.registerEvents(new FoodConsumeListener(), this);
		pm.registerEvents(new FoodLevelListener(), this);
		pm.registerEvents(new GrenadeListener(this), this);
		pm.registerEvents(new HealthRegainListener(), this);
		pm.registerEvents(new ItemDropListener(), this);
		pm.registerEvents(new JoinListener(this), this);
		pm.registerEvents(new PermissionsListener(), this);
		pm.registerEvents(new ShopListener(), this);
		pm.registerEvents(new SneakListener(), this);
	}

	private void registerGame() {
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		GameManager.getInstance().setPlugin(this);

		gameStart = new GameStart(this);
	}

}