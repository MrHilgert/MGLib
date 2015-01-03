package ru.hilgert.lib.mg.teams;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import ru.hilgert.lib.mg.arena.Arena;

public class GameTeam extends TeamManager {
	
	private List<String> teamPlayers = new ArrayList<>();
	private ChatColor color;
	private Color armColor;
	private int id;
	private Arena arena;
	private Location spawn;
	private ItemStack headItem;
	
	public GameTeam(int id, ChatColor color, Arena arena, Color armColor, Location spawn) {
		this.id = id;
		this.color = color;
		this.arena = arena;
		this.armColor = armColor;
		this.spawn = spawn;
	}

	public ChatColor getColor() {
		return color;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	public List<String> getTeamPlayers() {
		return teamPlayers;
	}

	public void setTeamPlayers(List<String> teamPlayers) {
		this.teamPlayers = teamPlayers;
	}

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}
	
	public void sendMessage(String msg) {
		for(String player : teamPlayers) {
			Bukkit.getPlayer(player).sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		}
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public Color getArmColor() {
		return armColor;
	}

	public void setArmColor(Color armColor) {
		this.armColor = armColor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ItemStack getHeadItem() {
		return headItem;
	}

	public void setHeadItem(ItemStack headItem) {
		this.headItem = headItem;
	}
	
}
