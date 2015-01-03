package ru.hilgert.lib.mg.events.arena;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.hilgert.lib.mg.arena.Arena;

public class MGArenaRegisterEvent extends Event implements Cancellable{

	private HandlerList handlers = new HandlerList();

	private Arena arena;
	
	private boolean isCancelled = false;
	
	public MGArenaRegisterEvent(Arena arena) {
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

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}

}