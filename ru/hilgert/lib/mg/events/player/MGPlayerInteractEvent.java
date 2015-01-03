package ru.hilgert.lib.mg.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import ru.hilgert.lib.mg.arena.Arena;

public class MGPlayerInteractEvent extends Event implements Cancellable {

	private static HandlerList handlers = new HandlerList();

	private Arena arena;
	private Player player;
	private Action action;

	private boolean cancelled;

	public MGPlayerInteractEvent(Arena arena, Player player, Action action) {
		this.arena = arena;
		this.player = player;
		this.action = action;
	}

	public Action getAction() {
		return action;
	}

	public ItemStack getItem() {
		return player.getItemInHand();
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
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
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}