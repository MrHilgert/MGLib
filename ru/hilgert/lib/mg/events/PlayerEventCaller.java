package ru.hilgert.lib.mg.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import ru.hilgert.lib.mg.arena.ArenaManager;
import ru.hilgert.lib.mg.events.player.MGPlayerDamageByPlayerEvent;
import ru.hilgert.lib.mg.events.player.MGPlayerDeathEvent;
import ru.hilgert.lib.mg.events.player.MGPlayerInteractEvent;

public class PlayerEventCaller implements Listener {

	private ArenaManager manager;
	private Plugin plugin;

	public PlayerEventCaller(Plugin plugin) {
		this.plugin = plugin;
		this.manager = new ArenaManager(this.plugin);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();
		if (manager.isPlaying(player)) {
			MGPlayerDeathEvent event = new MGPlayerDeathEvent(
					manager.getPlayerArena(player), player);
			callEvent(event);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (manager.isPlaying(player)) {
			MGPlayerInteractEvent event = new MGPlayerInteractEvent(
					manager.getPlayerArena(player), player, e.getAction());
			e.setCancelled(event.isCancelled());
			callEvent(event);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
			Player damager = (Player)e.getDamager();
			Player player = (Player)e.getEntity();
			if(manager.isPlaying(damager) && manager.isPlaying(player) && manager.getPlayerArena(player).equals(manager.getPlayerArena(damager))){
				MGPlayerDamageByPlayerEvent event = new MGPlayerDamageByPlayerEvent(manager.getPlayerArena(player), player, damager, e.getDamage(), e.getCause(), e.isCancelled());
				callEvent(event);
				e.setDamage(event.getDamage());
				e.setCancelled(event.isCancelled());
			}
		}
	}

	public void callEvent(Event e) {
		Bukkit.getPluginManager().callEvent(e);
	}

}
