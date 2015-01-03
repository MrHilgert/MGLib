package ru.hilgert.lib.mg.events.team;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.hilgert.lib.mg.arena.Arena;
import ru.hilgert.lib.mg.teams.GameTeam;

public class MGTeamRegisterEvent extends Event implements Cancellable{

	private HandlerList handlers = new HandlerList();

	private GameTeam team;
	
	private boolean isCancelled = false;

	public MGTeamRegisterEvent(GameTeam team) {
		this.team = team;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public int getId(){
		return team.getId();
	}
	
	public Color getArmColor(){
		return team.getArmColor();
	}
	
	public Arena getArena(){
		return team.getArena();
	}
	
	public ChatColor getColor(){
		return team.getColor();
	}
	
	public Location getSpawn(){
		return team.getSpawn();
	}
	
	public GameTeam getTeam(){
		return this.team;
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
