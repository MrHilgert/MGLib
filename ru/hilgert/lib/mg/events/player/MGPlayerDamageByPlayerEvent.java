package ru.hilgert.lib.mg.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ru.hilgert.lib.mg.arena.Arena;


public class MGPlayerDamageByPlayerEvent extends Event implements Cancellable{

	private HandlerList handlers = new HandlerList();

	private Arena arena;
	private Player player;
	private Player damager;
	private DamageCause cause;
	private int damage;
	
	private boolean cancelled;
	
	public MGPlayerDamageByPlayerEvent(Arena arena, Player player, Player damager, int damage, DamageCause cause, boolean cancelled) {
		this.arena = arena;
		this.player = player;
		this.damager = damager;
		this.cause = cause;
		this.damage = damage;
		this.cancelled = cancelled;
	}


	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Arena getArena() {
		return arena;
	}
	
	public Player getDamager(){
		return damager;
	}

	public Player getPlayer() {
		return player;
	}


	public DamageCause getCause() {
		return cause;
	}

	public int getDamage() {
		return damage;
	}


	public void setDamage(int damage) {
		this.damage = damage;
	}


	@Override
	public boolean isCancelled() {
		return cancelled;
	}


	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}