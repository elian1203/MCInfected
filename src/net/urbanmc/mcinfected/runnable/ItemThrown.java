package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.object.grenade.Grenade;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemThrown extends BukkitRunnable {

	private Grenade grenade;

	public ItemThrown(Grenade grenade, MCInfected plugin) {
		this.grenade = grenade;
		runTaskTimer(plugin, 0, 1);
	}

	@Override
	public void run() {
		boolean hitPlayer = itemHitPlayer(grenade.getItem());

		if (grenade.getItem().isOnGround() || hitPlayer) {
			grenade.activate();
			grenade.getItem().remove();
			cancel();
		}
	}

	private boolean itemHitPlayer(Item item) {
		return !item.getNearbyEntities(1, 2, 1).isEmpty();
	}
}
