package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.object.grenade.Grenade;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemThrown extends BukkitRunnable {

	private Grenade grenade;
	private MCInfected plugin;

	public ItemThrown(Grenade grenade, MCInfected plugin) {
		this.grenade = grenade;
		this.plugin = plugin;
		runTaskTimer(plugin, 0, 1);
	}

	@Override
	public void run() {
		if (grenade.getItem().isOnGround()) {
			System.out.print("Item is on ground!");
			grenade.getItem().remove();
			grenade.activate();

			cancel();
		}
	}
}
