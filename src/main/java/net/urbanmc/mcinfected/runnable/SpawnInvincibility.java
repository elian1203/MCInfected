package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.InvincibilityManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class SpawnInvincibility extends BukkitRunnable {

	private Map<UUID, Integer> map = InvincibilityManager.getInstance().getMap();

	SpawnInvincibility(MCInfected plugin) {
		runTaskTimerAsynchronously(plugin, 0, 20);
	}

	@Override
	public void run() {
		for (Entry<UUID, Integer> entry : map.entrySet()) {
			int time = entry.getValue();

			if (time > 0) {
				entry.setValue(time - 1);
			} else {
				map.remove(entry.getKey());
			}
		}
	}
}
