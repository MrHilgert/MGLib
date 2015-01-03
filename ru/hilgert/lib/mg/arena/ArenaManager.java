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

	public List<Arena> arenas = new ArrayList<>(); // Лист с аренами, которые
													// будут храниться временно

	private HashMap<Player, Arena> players = new HashMap<Player, Arena>();

	private Plugin plugin;

	public ArenaManager(Plugin plugin) {
		setPlugin(plugin);
	}

	public void addPlayer(Player p, Arena arena) { // Добавление игрока в арену

		if (arena == null) { // Проверка, существует ли арена
			p.sendMessage("§cАрена не найдена!");
			return;
		}

		MGPlayerJoinEvent event = new MGPlayerJoinEvent(arena, p);
		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled())
			return;

		if (event.getArena().isFull()) { // А она не заполнена?
			event.getPlayer().sendMessage(
					event.getArena().getPrefix() + "Арена заполнена!");
			return;
		}

		if (event.getArena().isInGame()) { // А если она в игре?
			event.getPlayer().sendMessage(
					event.getArena().getPrefix() + "Вы уже на арене!");
			return;
		}

		event.getPlayer().setHealth(event.getPlayer().getMaxHealth()); // Лечим
																		// игрока
		event.getPlayer().setFireTicks(0); // Еще лечим

		event.getPlayer().teleport(event.getArena().getLobby()); // Телепорт
																	// игрока в
																	// лобби

		event.getArena().getPlayers().add(event.getPlayer().getName()); // Добавляем
																		// игрока
																		// к
																		// игрокам
																		// на
		players.put(event.getPlayer(), event.getArena());

		// арене

		int playersLeft = event.getArena().getStart()
				- event.getArena().getPlayers().size();

		if (playersLeft == 0) { // Если это число == 0, то старт арены
			startArena(event.getArena());
		}

	}

	public void startArena(final Arena arena) { // Метод старта арены

		final MGArenaStartEvent event = new MGArenaStartEvent(arena);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;

		if (event.getArena().getPlayers() != null) // Проверяем, не повыходили
													// ли игроки?

			for (String str : event.getArena().getPlayers()) { // Берем все
																// имена игроков

				Player pl = Bukkit.getPlayer(str); // Превращяем их в игроков

				pl.teleport(event.getArena().getSpawn());

				event.getArena().setInGame(true); // Арена в игре!

				event.getArena().getWaiters().clear(); // Очищаем список
														// ожидающих

			}

		Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(),
				new Runnable() { // Запускаем шедулер, здесь мы его запустим,
									// чтобы через какое-то время арена
									// кончилась.

					public void run() {
						endArena(event.getArena()); // Метод завершения игры на
													// арене
					}

				}, event.getArena().getTime() * 20); // Время арены в тиках
														// умноженное на
		// 20, чтобы это были секунды

	}

	public void endArena(Arena arena) { // Метод завершения игры на арене
		if (arena == null) { // А существует ли арена?
			return;
		}

		MGArenaEndEvent event = new MGArenaEndEvent(arena);

		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled())
			return;

		if (event.getArena().getPlayers() != null) { // А на арене есть игроки?

			event.getArena().sendMessage("Время истекло!");
			event.getArena().setInGame(false); // Арена не в игре!

			Iterator<String> players = event.getArena().getPlayers().iterator();

			while (players.hasNext()) { // Пока у итератора не кончились ячейки
				String str = players.next(); // Тоже, что и for (String str :
												// arena.getPlayers())

				Player p = Bukkit.getPlayer(str); // Теперь это у нас игрок

				players.remove(); // Очищаем список играющих

				p.setHealth(p.getMaxHealth()); // Лечим игрока

				p.setFireTicks(0); // Еще лечим игрока

				removePlayer(p); // Убиарем игрока из арены

				p.teleport(event.getArena().getEnd());

			}

		}

	}

	public void removePlayer(Player p) { // Метод удаления игрока из арены
		for (Arena arena : arenas) { // Проходимя по всем аренам

			if (arena == null) { // Арена существует?
				p.sendMessage("§cАрена не существует!");
				return;
			}

			MGPlayerRemoveEvent event = new MGPlayerRemoveEvent(arena, p);

			Bukkit.getPluginManager().callEvent(event);

			if (event.getArena().getPlayers()
					.contains(event.getPlayer().getName())) { // А в ней есть
				// игрок?

				event.getPlayer().setHealth(event.getPlayer().getMaxHealth()); // Лечим,
																				// потом
																				// еще
				event.getPlayer().setFireTicks(0);

				event.getPlayer().teleport(event.getArena().getEnd());

				event.getArena().getPlayers()
						.remove(event.getPlayer().getName()); // Удаляем игрока
																// из
				// играющих

				event.getArena().sendMessage(
						event.getPlayer().getName() + " Вышел из арены! &6"
								+ event.getArena().getPlayers().size()
								+ "&4/&6" + event.getArena().getMax()); // Он
																		// покинул
																		// нас
																		// :c

				if (players.containsKey(event.getPlayer()))
					players.remove(event.getPlayer());

				if (event.getArena().getPlayers().size() <= 1) { // А на арене
																	// остался
					// только один, или нет
					// игроков вообще?
					// Заканчиваем арену.
					endArena(event.getArena());
				}

			}

		}

	}

	public void registerNewArena(String name, String prefix, int time,
			int start, int max, Location spawn, Location lobby, Location end) { // Как
																				// я
																				// и
																				// говорил,
																				// тут
																				// мы
																				// создаем
																				// арену,
																				// тоже
																				// самое,
																				// только
																				// тут
																				// мы
																				// ее
																				// еще
																				// будем
																				// хранить
																				// в
																				// листе

		Arena arena = new Arena(getPlugin(), name, prefix, time, start, max,
				spawn, lobby, end);

		MGArenaRegisterEvent event = new MGArenaRegisterEvent(arena);

		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled())
			return;

		arenas.add(event.getArena());

	}

	public boolean isInGame(Player p) { // Булевское значение, утилита. В игре
										// ли игрок?
		for (Arena a : arenas) {

			if (a.getPlayers().contains(p.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean isWaiter(Player p) { // А если игрок ожидающий? Еще одна
										// утилита.
		for (Arena a : arenas) {
			if (a.getWaiters().contains(p.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean hasPlayerInGame(Arena arena, Player p) { // Играет ли игрок
															// на определенной
															// арене?
		if (arena.getPlayers().contains(p.getName())
				&& !arena.getWaiters().contains(p.getName())) {
			return true;

		}
		return false;
	}

	public boolean isPlaying(Player player) {
		return players.containsKey(player);
	}

	public Arena getPlayerArena(Player player) {
		return players.get(player);
	}

	public boolean hasWaiter(Arena arena, Player p) { // А может, он только
														// ожидающий на этой
														// арене?
		if (!arena.getPlayers().contains(p.getName())
				&& arena.getWaiters().contains(p.getName())) {
			return true;
		}
		return false;
	}

	public Arena getArena(String name) { // Найдем арену по имени

		for (Arena a : arenas) {
			if (a.getName().equalsIgnoreCase(name)) {
				return a;
			}
		}
		return null;
	}

	public Arena getPlayingArena(Player p) { // В какой же арене играет игрок?
												// Утилита.
		for (Arena a : arenas) {
			if (a.getPlayers().contains(p.getName())) {
				return a;
			}
		}
		return null;
	}

	public void removeArena(Arena arena) { // Метод удаления арены
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
