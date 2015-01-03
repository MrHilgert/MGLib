package ru.hilgert.lib.mg.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class Arena extends ArenaManager {

	private List<String> players = new ArrayList<>(); // Лист с игроками
	private int time; // Время арены
	private int start; // Кол-во игроков для старта
	private int max; // Максимальное кол-во игроков
	private String name; // Имя арены
	private String prefix; // Префикс арены
	private Location spawn, lobby, end; // Локации
	private List<String> waiters = new ArrayList<>(); // Лист с ожидающими
	private boolean inGame; // Булевское значение - в игре

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

	} // Функция для создания арены. Для этого нам потребуются некоторые
		// аргументы. Чтобы создать арену, нам нужно будет сделать переменную
		// арены типа этой: Arena a = new Arena(...); , и затем пользоваться ей,
		// но так арену мы создавать не будем, будем создавать ее через
		// ArenaManager, об этом чуть позже

	public List<String> getPlayers() { // Метод getPlayers() возвращает лист
										// игроков, который можно изменять.
		return players;
	}

	public int getTime() {// Метод getTime() возвращает время арены
		return time;
	}

	public void setTime(int time) { // Установка времени
		this.time = time;
	}

	public int getStart() { // Возвращает кол-во игроков для старта
		return start;
	}

	public void setStart(int start) { // Установка кол-ва игроков для старта
		this.start = start;
	}

	public int getMax() { // Возвращает кол-во макс. игроков
		return max;
	}

	public void setMax(int max) { // Установка числа макс. игроков
		this.max = max;
	}

	public String getName() { // Возвращает имя
		return name;
	}

	public void setName(String name) { // Установка имени
		this.name = name;
	}

	public Location getSpawn() { // Возвращает локацию спавна
		return spawn;
	}

	public void setSpawn(Location spawn) { // Установка локации спавна
		this.spawn = spawn;
	}

	public Location getLobby() { // Возвращает локацию лобби
		return lobby;
	}

	public void setLobby(Location lobby) { // Установка локации лобби
		this.lobby = lobby;
	}

	public Location getEnd() { // Возвращает локацию конца игры
		return end;
	}

	public void setEnd(Location end) { // Установка локации конца игры
		this.end = end;
	}

	public boolean isFull() { // Булевское значение, которое проверяет, полная
								// ли арена

		return players.size() >= max;

	}

	public void sendMessage(String msg) {
		for (String str : players) {
			Bukkit.getPlayer(str).sendMessage(ChatColor.translateAlternateColorCodes('&', getPrefix() + msg));
		}
	}

	public boolean isInGame() { // Проверка, в игре ли арена
		return inGame;
	}

	public void setInGame(boolean inGame) { // Установка булевского значения
											// "в игре"
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
