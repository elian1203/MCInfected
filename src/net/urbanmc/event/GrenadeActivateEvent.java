package net.urbanmc.event;

import net.urbanmc.mcinfected.object.grenade.Grenade;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GrenadeActivateEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;

	private Grenade grenade;

	public GrenadeActivateEvent(Grenade grenade) {
		this.grenade = grenade;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Grenade getGrenade() {
		return grenade;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
