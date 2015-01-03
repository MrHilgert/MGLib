package ru.hilgert.lib.mg.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import ru.hilgert.lib.mg.events.arena.MGArenaEndEvent;
import ru.hilgert.lib.mg.events.arena.MGArenaRegisterEvent;
import ru.hilgert.lib.mg.events.arena.MGArenaRemoveEvent;
import ru.hilgert.lib.mg.events.arena.MGArenaStartEvent;
import ru.hilgert.lib.mg.events.player.MGPlayerJoinEvent;
import ru.hilgert.lib.mg.events.player.MGPlayerRemoveEvent;

public class ArenaManager {

	public List<Arena> arenas = new ArrayList<>(); // ���� � �������, �������
													// ����� ��������� ��������
	
	private HashMap<Player, Arena> players = new HashMap<Player, Arena>();
	
	private Plugin plugin;

	public ArenaManager(Plugin plugin) {
		setPlugin(plugin);
	}

	public void addPlayer(Player p, Arena arena) { // ���������� ������ � �����

		if (arena == null) { // ��������, ���������� �� �����
			p.sendMessage("�c����� �� �������!");
			return;
		}

		MGPlayerJoinEvent event = new MGPlayerJoinEvent(arena, p);
		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled())
			return;

		if (event.getArena().isFull()) { // � ��� �� ���������?
			event.getPlayer().sendMessage(
					event.getArena().getPrefix() + "����� ���������!");
			return;
		}

		if (event.getArena().isInGame()) { // � ���� ��� � ����?
			event.getPlayer().sendMessage(
					event.getArena().getPrefix() + "�� ��� �� �����!");
			return;
		}

		event.getPlayer().setHealth(event.getPlayer().getMaxHealth()); // �����
																		// ������
		event.getPlayer().setFireTicks(0); // ��� �����

		event.getPlayer().teleport(event.getArena().getLobby()); // ��������
																	// ������ �
																	// �����

		event.getArena().getPlayers().add(event.getPlayer().getName()); // ���������
																		// ������
																		// �
																		// �������
																		// ��
		players.put(event.getPlayer(), event.getArena());
		
		// �����

		int playersLeft = event.getArena().getStart()
				- event.getArena().getPlayers().size();

		if (playersLeft == 0) { // ���� ��� ����� == 0, �� ����� �����
			startArena(event.getArena());
		}

	}

	public void startArena(final Arena arena) { // ����� ������ �����

		final MGArenaStartEvent event = new MGArenaStartEvent(arena);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;

		if (event.getArena().getPlayers() != null) // ���������, �� ����������
													// �� ������?

			for (String str : event.getArena().getPlayers()) { // ����� ���
																// ����� �������

				Player pl = Bukkit.getPlayer(str); // ���������� �� � �������

				pl.teleport(event.getArena().getSpawn());

				event.getArena().setInGame(true); // ����� � ����!

				event.getArena().getWaiters().clear(); // ������� ������
														// ���������

			}

		Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(),
				new Runnable() { // ��������� �������, ����� �� ��� ��������,
									// ����� ����� �����-�� ����� �����
									// ���������.

					public void run() {
						endArena(event.getArena()); // ����� ���������� ���� ��
													// �����
					}

				}, event.getArena().getTime() * 20); // ����� ����� � �����
														// ���������� ��
		// 20, ����� ��� ���� �������

	}

	public void endArena(Arena arena) { // ����� ���������� ���� �� �����
		if (arena == null) { // � ���������� �� �����?
			return;
		}

		MGArenaEndEvent event = new MGArenaEndEvent(arena);

		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled())
			return;

		if (event.getArena().getPlayers() != null) { // � �� ����� ���� ������?

			event.getArena().sendMessage(
					event.getArena().getPrefix() + "The end of the game!");
			event.getArena().setInGame(false); // ����� �� � ����!

			Iterator<String> players = event.getArena().getPlayers().iterator();

			while (players.hasNext()) { // ���� � ��������� �� ��������� ������
				String str = players.next(); // ����, ��� � for (String str :
												// arena.getPlayers())

				Player p = Bukkit.getPlayer(str); // ������ ��� � ��� �����

				players.remove(); // ������� ������ ��������

				p.setHealth(p.getMaxHealth()); // ����� ������

				p.setFireTicks(0); // ��� ����� ������

				removePlayer(p); // ������� ������ �� �����

				p.teleport(event.getArena().getEnd());

			}

		}

	}

	public void removePlayer(Player p) { // ����� �������� ������ �� �����
		for (Arena arena : arenas) { // ��������� �� ���� ������

			if (arena == null) { // ����� ����������?
				p.sendMessage("�c����� �� ����������!");
				return;
			}

			MGPlayerRemoveEvent event = new MGPlayerRemoveEvent(arena, p);

			Bukkit.getPluginManager().callEvent(event);

			if (event.getArena().getPlayers()
					.contains(event.getPlayer().getName())) { // � � ��� ����
				// �����?

				event.getPlayer().setHealth(event.getPlayer().getMaxHealth()); // �����,
																				// �����
																				// ���
				event.getPlayer().setFireTicks(0);

				event.getPlayer().teleport(event.getArena().getEnd());

				event.getArena().getPlayers()
						.remove(event.getPlayer().getName()); // ������� ������
																// ��
				// ��������

				event.getArena().sendMessage(
						event.getArena().getPrefix()
								+ event.getPlayer().getName()
								+ " ����� �� �����! &6"
								+ event.getArena().getPlayers().size()
								+ "&4/&6" + event.getArena().getMax()); // ��
																		// �������
																		// ���
																		// :c
				
				if(players.containsKey(event.getPlayer())) players.remove(event.getPlayer());
				
				if (event.getArena().getPlayers().size() <= 1) { // � �� �����
																	// �������
					// ������ ����, ��� ���
					// ������� ������?
					// ����������� �����.
					endArena(event.getArena());
				}

			}

		}

	}

	public void registerNewArena(String name, String prefix, int time,
			int start, int max, Location spawn, Location lobby, Location end) { // ���
																				// �
																				// �
																				// �������,
																				// ���
																				// ��
																				// �������
																				// �����,
																				// ����
																				// �����,
																				// ������
																				// ���
																				// ��
																				// ��
																				// ���
																				// �����
																				// �������
																				// �
																				// �����

		Arena arena = new Arena(getPlugin(), name, prefix, time, start, max,
				spawn, lobby, end);

		MGArenaRegisterEvent event = new MGArenaRegisterEvent(arena);

		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled())
			return;

		arenas.add(event.getArena());

	}

	public boolean isInGame(Player p) { // ��������� ��������, �������. � ����
										// �� �����?
		for (Arena a : arenas) {

			if (a.getPlayers().contains(p.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean isWaiter(Player p) { // � ���� ����� ���������? ��� ����
										// �������.
		for (Arena a : arenas) {
			if (a.getWaiters().contains(p.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean hasPlayerInGame(Arena arena, Player p) { // ������ �� �����
															// �� ������������
															// �����?
		if (arena.getPlayers().contains(p.getName())
				&& !arena.getWaiters().contains(p.getName())) {
			return true;

		}
		return false;
	}

	public boolean isPlaying(Player player){
		return players.containsKey(player);
	}
	
	public Arena getPlayerArena(Player player){
		return players.get(player);
	}
	
	public boolean hasWaiter(Arena arena, Player p) { // � �����, �� ������
														// ��������� �� ����
														// �����?
		if (!arena.getPlayers().contains(p.getName())
				&& arena.getWaiters().contains(p.getName())) {
			return true;
		}
		return false;
	}

	public Arena getArena(String name) { // ������ ����� �� �����

		for (Arena a : arenas) {
			if (a.getName().equalsIgnoreCase(name)) {
				return a;
			}
		}
		return null;
	}

	public Arena getPlayingArena(Player p) { // � ����� �� ����� ������ �����?
												// �������.
		for (Arena a : arenas) {
			if (a.getPlayers().contains(p.getName())) {
				return a;
			}
		}
		return null;
	}

	public void removeArena(Arena arena) { // ����� �������� �����
		if (arena != null) {
			MGArenaRemoveEvent event = new MGArenaRemoveEvent(arena);

			Bukkit.getPluginManager().callEvent(event);

			if (event.isCancelled())
				return;
			arenas.remove(event.getArena());
		}
	}
	
	

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

}
