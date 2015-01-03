package ru.hilgert.lib.mg.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class Arena extends ArenaManager {

	private List<String> players = new ArrayList<>(); // ���� � ��������
	private int time; // ����� �����
	private int start; // ���-�� ������� ��� ������
	private int max; // ������������ ���-�� �������
	private String name; // ��� �����
	private String prefix; // ������� �����
	private Location spawn, lobby, end; // �������
	private List<String> waiters = new ArrayList<>(); // ���� � ����������
	private boolean inGame; // ��������� �������� - � ����

	public Arena(Plugin plugin, String name, String prefix, int time, int start, int max,
			Location spawn, Location lobby, Location end) {
		super(plugin);
		setTime(time);
		setMax(max);
		setName(name);
		setSpawn(spawn);
		setLobby(lobby);
		setEnd(end);
		setPrefix(prefix);

	} // ������� ��� �������� �����. ��� ����� ��� ����������� ���������
		// ���������. ����� ������� �����, ��� ����� ����� ������� ����������
		// ����� ���� ����: Arena a = new Arena(...); , � ����� ������������ ��,
		// �� ��� ����� �� ��������� �� �����, ����� ��������� �� �����
		// ArenaManager, �� ���� ���� �����

	public List<String> getPlayers() { // ����� getPlayers() ���������� ����
										// �������, ������� ����� ��������.
		return players;
	}

	public int getTime() {// ����� getTime() ���������� ����� �����
		return time;
	}

	public void setTime(int time) { // ��������� �������
		this.time = time;
	}

	public int getStart() { // ���������� ���-�� ������� ��� ������
		return start;
	}

	public void setStart(int start) { // ��������� ���-�� ������� ��� ������
		this.start = start;
	}

	public int getMax() { // ���������� ���-�� ����. �������
		return max;
	}

	public void setMax(int max) { // ��������� ����� ����. �������
		this.max = max;
	}

	public String getName() { // ���������� ���
		return name;
	}

	public void setName(String name) { // ��������� �����
		this.name = name;
	}

	public Location getSpawn() { // ���������� ������� ������
		return spawn;
	}

	public void setSpawn(Location spawn) { // ��������� ������� ������
		this.spawn = spawn;
	}

	public Location getLobby() { // ���������� ������� �����
		return lobby;
	}

	public void setLobby(Location lobby) { // ��������� ������� �����
		this.lobby = lobby;
	}

	public Location getEnd() { // ���������� ������� ����� ����
		return end;
	}

	public void setEnd(Location end) { // ��������� ������� ����� ����
		this.end = end;
	}

	public boolean isFull() { // ��������� ��������, ������� ���������, ������
								// �� �����

		return players.size() >= max;

	}

	public void sendMessage(String msg) {
		for (String str : players) {
			Bukkit.getPlayer(str).sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		}
	}

	public boolean isInGame() { // ��������, � ���� �� �����
		return inGame;
	}

	public void setInGame(boolean inGame) { // ��������� ���������� ��������
											// "� ����"
		this.inGame = inGame;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public List<String> getWaiters() {
		return waiters;
	}

	public void setWaiters(List<String> waiters) {
		this.waiters = waiters;
	}

}
