package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.object.grenade.Grenade;
import org.bukkit.Bukkit;

public class ItemThrown implements Runnable {

	private int taskId;
	private Grenade grenade;

	public ItemThrown(Grenade grenade) {
		this.grenade = grenade;
	}

	@Override
	public void run() {
		if (grenade.getItem().isOnGround()) {
			grenade.activate();
		}
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	private void cancel() {
		Bukkit.getScheduler().cancelTask(taskId);
	}
}
