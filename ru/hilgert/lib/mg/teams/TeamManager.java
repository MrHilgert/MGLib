package ru.hilgert.lib.mg.teams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import ru.hilgert.lib.mg.arena.Arena;
import ru.hilgert.lib.mg.events.team.MGTeamPlayerAddEvent;
import ru.hilgert.lib.mg.events.team.MGTeamPlayerRemoveEvent;
import ru.hilgert.lib.mg.events.team.MGTeamRegisterEvent;
import ru.hilgert.lib.mg.events.team.MGTeamRemoveEvent;

public class TeamManager {

	private static TeamManager tm = new TeamManager();
	public List<GameTeam> teams = new ArrayList<>();

	public static TeamManager getManager() {
		return tm;
	}

	public void addPlayer(final GameTeam team, final Player p) {
		if (team != null) {

			if (team.getArena() == null) {
				p.sendMessage("Арены нет!");
				return;
			}

			if (team.hasPlayer(team, p)) {
				p.sendMessage("Вы уже находитесь в этой команде!");
				return;
			}

			MGTeamPlayerAddEvent event = new MGTeamPlayerAddEvent(team.getArena(),
					team, p);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;
			
			p.sendMessage("Вы присоединились к команде " + team.getColor()
					+ team.getColor().name().replace("DARK_", "") + "§f!");
			team.getTeamPlayers().add(p.getName());

		}
	}

	@SuppressWarnings("deprecation")
	public void addPlayerHard(GameTeam team, Player p) {

		if (team.getArena() == null) {
			p.sendMessage("Арены нет!");
			return;
		}

		MGTeamPlayerAddEvent event = new MGTeamPlayerAddEvent(team.getArena(),
				team, p);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;

		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta meta = (LeatherArmorMeta) chest.getItemMeta();
		meta.setColor(team.getArmColor());
		chest.setItemMeta(meta);

		ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta meta1 = (LeatherArmorMeta) leg.getItemMeta();
		meta1.setColor(team.getArmColor());
		leg.setItemMeta(meta1);

		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta meta2 = (LeatherArmorMeta) boots.getItemMeta();
		meta2.setColor(team.getArmColor());
		boots.setItemMeta(meta2);

		p.getInventory().setHelmet(new ItemStack(team.getHeadItem()));
		p.getInventory().setChestplate(chest);
		p.getInventory().setLeggings(leg);
		p.getInventory().setBoots(boots);
		p.updateInventory();

		p.teleport(team.getSpawn());
	}

	public void removePlayer(GameTeam team, Player p) {
		if (team != null) {

			if (team.getArena() == null) {
				p.sendMessage("Арены нет!");
				return;
			}

			MGTeamPlayerRemoveEvent event = new MGTeamPlayerRemoveEvent(
					team.getArena(), team, p);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;

			team.getTeamPlayers().remove(p.getName());

		}
	}

	@SuppressWarnings("deprecation")
	public void removePlayerHard(GameTeam team, Player p) {
		if (team != null) {

			if (team.getArena() == null) {
				p.sendMessage("Арены нет!");
				return;
			}

			MGTeamPlayerRemoveEvent event = new MGTeamPlayerRemoveEvent(
					team.getArena(), team, p);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;

			team.getTeamPlayers().remove(p.getName());

			p.getInventory().setHelmet(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setBoots(null);
			p.updateInventory();
			p.setPlayerListName(p.getName());
			p.setDisplayName(p.getName());

		}
	}

	public GameTeam registerNewTeam(int id, ChatColor color, Arena arena,
			Color armColor, Location spawn) {
		GameTeam team = new GameTeam(id, color, arena, armColor, spawn);

		MGTeamRegisterEvent event = new MGTeamRegisterEvent(team);
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled())
			teams.add(team);

		return team;
	}

	public void removeTeam(GameTeam team) {
		if (teams.contains(team)) {
			MGTeamRemoveEvent event = new MGTeamRemoveEvent(team);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled()) return;
			teams.remove(team);
		}
		return;
	}

	public GameTeam getTeamByColor(ChatColor color) {
		for (GameTeam t : teams) {
			if (t != null && t.getColor() == color) {
				return t;
			}
		}
		return null;
	}

	public GameTeam getTeamById(Arena a, int id) {
		for (GameTeam t : teams) {
			if (t != null && t.getId() == id && t.getArena() == a) {
				return t;
			}
		}
		return null;
	}

	public GameTeam getTeamByName(Arena a, String name) {
		for (GameTeam t : teams) {
			if (t != null && t.getColor().name() == name && t.getArena() == a) {
				return t;
			}
		}
		return null;
	}

	public GameTeam getPlayingTeam(Player p) {
		for (GameTeam t : teams) {
			if (t != null && t.getTeamPlayers().contains(p.getName())) {
				return t;
			}
		}
		return null;
	}

	public boolean hasTeam(Arena arena, GameTeam team) {
		if (team.getArena() == arena) {
			return true;
		}
		return false;
	}

	public boolean hasPlayer(GameTeam team, Player p) {
		if (team.getTeamPlayers().contains(p.getName())) {
			return true;
		}
		return false;
	}

	public boolean isInTeam(Player p) {
		for (GameTeam t : teams) {
			if (t.getTeamPlayers().contains(p.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean isInTeam(GameTeam team, Player p) {
		if (team.getTeamPlayers().contains(p.getName())) {
			return true;
		}
		return false;
	}

	public void saveTeam(GameTeam team, File f, Arena arena, Location spawn) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		setLoc(arena.getName(), team, spawn, config);

		try {
			config.save(f);
			config.load(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadTeam(int id, File f, Arena arena) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		try {
			config.load(f);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Location loc = getLoc(arena.getName(), id, config);

		registerNewTeam(id, null, arena, null, loc);

	}

	public void removeTeam(File f, int id, Arena arena) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		if (config.getConfigurationSection("arenas." + arena.getName()
				+ ".teams" + id) != null) {
			config.set("arenas." + arena.getName() + ".teams." + id, null);
		}

		try {
			config.save(f);
			config.load(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setLoc(String name, GameTeam team, Location loc,
			FileConfiguration config) {
		config.set("arenas." + name + ".teams." + team.getId() + ".world", loc
				.getWorld().getName());
		config.set("arenas." + name + ".teams." + team.getId() + ".x",
				loc.getX());
		config.set("arenas." + name + ".teams." + team.getId() + ".y",
				loc.getY());
		config.set("arenas." + name + ".teams." + team.getId() + ".z",
				loc.getZ());
	}

	private static Location getLoc(String name, int id, FileConfiguration config) {
		return new Location(Bukkit.getWorld(config.getString("arenas." + name
				+ ".teams." + id + ".world")), config.getDouble("arenas."
				+ name + ".teams." + id + ".x"), config.getDouble("arenas."
				+ name + ".teams." + id + ".y"), config.getDouble("arenas."
				+ name + ".teams." + id + ".z"));
	}

}
