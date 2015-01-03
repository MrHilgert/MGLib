package ru.hilgert.lib.mg.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.hilgert.lib.mg.arena.Arena;

public class MGPlayerDeathEvent extends Event{

	private HandlerList handlers = new HandlerList();

	private Arena arena;
	private Player player;
	
	
	public MGPlayerDeathEvent(Arena arena, Player player) {
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

}