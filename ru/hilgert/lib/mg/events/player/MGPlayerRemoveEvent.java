package ru.hilgert.lib.mg.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.hilgert.lib.mg.arena.Arena;

public class MGPlayerRemoveEvent extends Event implements Cancellable {

	private static HandlerList handlers = new HandlerList();

	private Arena arena;
	private Player player;

	private boolean isCancelled = false;

	public MGPlayerRemoveEvent(Arena arena, Player player) {
		this.arena = arena;
		this.player = player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Arena getArena() {
		return arena;
	}

	public Player getPlayer() {
		return player;
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