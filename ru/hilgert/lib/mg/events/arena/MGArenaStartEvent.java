package ru.hilgert.lib.mg.events.arena;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.hilgert.lib.mg.arena.Arena;

public class MGArenaStartEvent extends Event implements Cancellable {

	private static HandlerList handlers = new HandlerList();

	private Arena arena;

	private boolean isCancelled = false;

	public MGArenaStartEvent(Arena arena) {
		this.arena = arena;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Arena getArena() {
		return arena;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}

}
