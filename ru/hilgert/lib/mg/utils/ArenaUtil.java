package ru.hilgert.lib.mg.utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import ru.hilgert.lib.mg.MGLib;
import ru.hilgert.lib.mg.MiniGame;
import ru.hilgert.lib.mg.arena.Arena;
import ru.hilgert.lib.mg.arena.ArenaManager;

public class ArenaUtil {

	private static ArenaManager am;

	private static String miniGame = "";
	
	private static MGLib mglib = new MGLib();
	
	public ArenaUtil(MiniGame mg, ArenaManager am) {
		setArenaManager(am);
		if(!mglib.getMiniGamesImplements().containsKey(mg)){
			throw new NullPointerException("MiniGame not found");
		}
		miniGame = mglib.getMiniGamesImplements().get(mg);
	}

	public static void saveArena(Arena arena, File f) { // Метод сохранения
														// арены. Два аргумента
														// - арена, и файл, куда
														// сохранять.

		String name = arena.getName(); // Имя арены. Дальше просто разделяем
										// каждую составляющую арены на части.

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f); // Наша
																			// конфигурация

		config.set("arenas." + name + ".prefix",
				arena.getPrefix().replace("&", "§"));

		config.set("arenas." + name + ".spawn.loc.world", arena.getSpawn()
				.getWorld().getName());
		config.set("arenas." + name + ".spawn.loc.x", arena.getSpawn().getX());
		config.set("arenas." + name + ".spawn.loc.y", arena.getSpawn().getY());
		config.set("arenas." + name + ".spawn.loc.z", arena.getSpawn().getZ());

		config.set("arenas." + name + ".lobby.loc.world", arena.getLobby()
				.getWorld().getName());
		config.set("arenas." + name + ".lobby.loc.x", arena.getLobby().getX());
		config.set("arenas." + name + ".lobby.loc.y", arena.getLobby().getY());
		config.set("arenas." + name + ".lobby.loc.z", arena.getLobby().getZ());

		config.set("arenas." + name + ".end.loc.world", arena.getEnd()
				.getWorld().getName());
		config.set("arenas." + name + ".end.loc.x", arena.getEnd().getX());
		config.set("arenas." + name + ".end.loc.y", arena.getEnd().getY());
		config.set("arenas." + name + ".end.loc.z", arena.getEnd().getZ());

		config.set("arenas." + name + ".time", arena.getTime());
		config.set("arenas." + name + ".start", arena.getStart());
		config.set("arenas." + name + ".max", arena.getMax());

		try {
			config.save(f);
			config.load(f);
		} catch (Exception e1) {
			e1.printStackTrace();
		} // Типа сохранили, и подгрузили конфиг.
	}

	public static void loadArena(String name, File f) { // Загружаем арену, но
														// тут в аргументах не
														// арена, а строка,
														// какую мы будем
														// подгружать.
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f); // Наша
																			// конфигурация

		// Дальше идут три наших локации арены

		Location spawn = new Location(Bukkit.getWorld(config
				.getString("arenas." + name + ".spawn.loc.world")),
				config.getDouble("arenas." + name + ".spawn.loc.x"),
				config.getDouble("arenas." + name + ".spawn.loc.y"),
				config.getDouble("arenas." + name + ".spawn.loc.z"));

		Location lobby = new Location(Bukkit.getWorld(config
				.getString("arenas." + name + ".lobby.loc.world")),
				config.getDouble("arenas." + name + ".lobby.loc.x"),
				config.getDouble("arenas." + name + ".lobby.loc.y"),
				config.getDouble("arenas." + name + ".lobby.loc.z"));

		Location end = new Location(Bukkit.getWorld(config.getString("arenas."
				+ name + ".end.loc.world")), config.getDouble("arenas." + name
				+ ".end.loc.x"), config.getDouble("arenas." + name
				+ ".end.loc.y"), config.getDouble("arenas." + name
				+ ".end.loc.z"));

		if (config.getConfigurationSection("arenas." + name) != null) {
			getArenaManager()
					.registerNewArena(
							name,
							config.getString("arenas." + name + ".prefix")
									.replace("&", "§"),
							config.getInt("arenas." + name + ".time"),
							config.getInt("arenas." + name + ".start"),
							config.getInt("arenas." + name + ".max"), spawn,
							lobby, end);

			// Тут мы создали, и подгрузили арену.

			getArenaManager().getArena(name).setInGame(false);
			getArenaManager().getArena(name).setSpawn(spawn);
			getArenaManager().getArena(name).setEnd(end);
			getArenaManager().getArena(name).setLobby(lobby);
			getArenaManager().getArena(name).setPrefix(
					config.getString("arenas." + name + ".prefix").replace("&",
							"§"));
			getArenaManager().getArena(name).setTime(
					config.getInt("arenas." + name + ".time"));
			getArenaManager().getArena(name).setMax(
					config.getInt("arenas." + name + ".max"));
			getArenaManager().getArena(name).setStart(
					config.getInt("arenas." + name + ".start"));

			// И так, тут мы уже добавляем арену во временное хранение, в
			// пожизненном хранении никуда ее не деваем.

		}
	}

	public static void loadAllArenas(File f) { // Метод, для удобной подгрузки
												// всех арен из конфигурации в
												// методе onEnable()

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f); // Наша
																			// конфигурация

		if (config.getConfigurationSection("arenas") != null) { // Проверяем,
																// есть ли в
																// конфиге
																// секция
																// "arenas"

			for (String an : config.getConfigurationSection("arenas")
					.getValues(false).keySet()) { // Проходимся по всем
													// подсекциям в секции
													// "arenas"
				loadArena(an, f); // И тут загружаем каждую
			}

		}
	}

	// Далее пойдут методы для каждой функции, которые будет производиться не во
	// временном, а в пожизненном хранении.

	public static void removeArena(String name, File f) { // Метод удаления
															// арены

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		if (config.getConfigurationSection("arenas." + name) != null) { // Есть
																		// ли
																		// секция
																		// arenas.name?
			config.set("arenas." + name, null); // Теперь эта секция - null
		}

		try {
			config.save(f);
			config.load(f);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void setStart(int i, Arena arena, File f) { // Метод для
																// установки
																// кол-ва
																// игроков для
																// старта

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		if (config.getConfigurationSection("arenas." + arena.getName()) != null) {
			config.set("arenas." + arena.getName() + ".start", i);
		}

		try {
			config.save(f);
			config.load(f);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void setMax(int i, Arena arena, File f) { // Метод для
															// установки макс.
															// игроков

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		if (config.getConfigurationSection("arenas." + arena.getName()) != null) {
			config.set("arenas." + arena.getName() + ".max", i);
		}

		try {
			config.save(f);
			config.load(f);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void setTime(int i, Arena arena, File f) { // Метод установки
																// времени для
																// арены

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		if (config.getConfigurationSection("arenas." + arena.getName()) != null) {
			config.set("arenas." + arena.getName() + ".time", i);
		}

		try {
			config.save(f);
			config.load(f);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void setSpawn(Location loc, Arena arena, File f) { // Метод
																		// установки
																		// локации
																		// спавна

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		String name = arena.getName();

		if (config.getConfigurationSection("arenas." + arena.getName()) != null) { // Есть
																					// такая
																					// секция?
																					// Далее
																					// разбиваем
																					// локацию
																					// на
																					// мир,
																					// x,
																					// y
																					// ,z.
			config.set("arenas." + name + ".spawn.loc.world", arena.getSpawn()
					.getWorld().getName());
			config.set("arenas." + name + ".spawn.loc.x", arena.getSpawn()
					.getX());
			config.set("arenas." + name + ".spawn.loc.y", arena.getSpawn()
					.getY());
			config.set("arenas." + name + ".spawn.loc.z", arena.getSpawn()
					.getZ());
		}

		try {
			config.save(f);
			config.load(f);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void setLobby(Location loc, Arena arena, File f) { // Метод
																		// установки
																		// локации
																		// лобби

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		String name = arena.getName();

		if (config.getConfigurationSection("arenas." + arena.getName()) != null) { // Есть
																					// такая
																					// секция?
																					// Далее
																					// разбиваем
																					// локацию
																					// на
																					// мир,
																					// x,
																					// y
																					// ,z.
			config.set("arenas." + name + ".lobby.loc.world", arena.getLobby()
					.getWorld().getName());
			config.set("arenas." + name + ".lobby.loc.x", arena.getLobby()
					.getX());
			config.set("arenas." + name + ".lobby.loc.y", arena.getLobby()
					.getY());
			config.set("arenas." + name + ".lobby.loc.z", arena.getLobby()
					.getZ());
		}

		try {
			config.save(f);
			config.load(f);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void setEnd(Location loc, Arena arena, File f) { // Метод
																	// установки
																	// локации
																	// конца
																	// игры

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		String name = arena.getName();

		if (config.getConfigurationSection("arenas." + arena.getName()) != null) { // Есть
																					// такая
																					// секция?
																					// Далее
																					// разбиваем
																					// локацию
																					// на
																					// мир,
																					// x,
																					// y
																					// ,z.
			config.set("arenas." + name + ".end.loc.world", arena.getEnd()
					.getWorld().getName());
			config.set("arenas." + name + ".end.loc.x", arena.getEnd().getX());
			config.set("arenas." + name + ".end.loc.y", arena.getEnd().getY());
			config.set("arenas." + name + ".end.loc.z", arena.getEnd().getZ());
		}

		try {
			config.save(f);
			config.load(f);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static ArenaManager getArenaManager() {
		return am;
	}

	public static void setArenaManager(ArenaManager am) {
		ArenaUtil.am = am;
	}

}
