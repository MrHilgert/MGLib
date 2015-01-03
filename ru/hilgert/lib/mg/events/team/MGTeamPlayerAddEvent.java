package ru.hilgert.lib.mg.events.team;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.hilgert.lib.mg.arena.Arena;
import ru.hilgert.lib.mg.teams.GameTeam;

public class MGTeamPlayerAddEvent extends Event implements Cancellable{

	private HandlerList handlers = new HandlerList();

	private Arena arena;
	private Player player;
	private GameTeam team;
	
	private boolean isCancelled = false;
	
	public MGTeamPlayerAddEvent(Arena arena, GameTeam team, Player player) {
		this.arena = arena;
		this.player = player;
		this.team = team;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public GameTeam getTeam(){
		return this.team;
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

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}

}