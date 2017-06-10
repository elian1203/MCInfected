package net.urbanmc.mcinfected.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawn implements Listener {

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		e.setCancelled(true);
	}
}
