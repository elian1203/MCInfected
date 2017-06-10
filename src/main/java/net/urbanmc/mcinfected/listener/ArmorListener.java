package net.urbanmc.mcinfected.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;

public class ArmorListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getSlotType().equals(SlotType.ARMOR)) {
			e.setCancelled(true);
		}
	}
}
