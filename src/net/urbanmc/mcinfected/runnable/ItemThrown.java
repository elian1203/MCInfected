package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.object.grenade.Grenade;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemThrown extends BukkitRunnable {

	private Grenade grenade;

	public ItemThrown(Grenade grenade) {
		this.grenade = grenade;
	}

	@Override
	public void run() {
		if (grenade.getItem().isOnGround()) {
			grenade.getItem().remove();
			grenade.activate();

			cancel();
		}
	}
}
