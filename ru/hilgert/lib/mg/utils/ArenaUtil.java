package ru.hilgert.lib.mg.utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import ru.hilgert.lib.mg.arena.Arena;
import ru.hilgert.lib.mg.arena.ArenaManager;

public class ArenaUtil {

	private static ArenaManager am;

	public ArenaUtil(ArenaManager am) {
		setArenaManager(am);
	}

	public static void saveArena(Arena arena, File f) { // ����� ����������
														// �����. ��� ���������
														// - �����, � ����, ����
														// ���������.

		String name = arena.getName(); // ��� �����. ������ ������ ���������
										// ������ ������������ ����� �� �����.

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f); // ����
																			// ������������

		config.set("arenas." + name + ".prefix",
				arena.getPrefix().replace("&", "�"));

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
		} // ���� ���������, � ���������� ������.
	}

	public static void loadArena(String name, File f) { // ��������� �����, ��
														// ��� � ���������� ��
														// �����, � ������,
														// ����� �� �����
														// ����������.
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f); // ����
																			// ������������

		// ������ ���� ��� ����� ������� �����

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
									.replace("&", "�"),
							config.getInt("arenas." + name + ".time"),
							config.getInt("arenas." + name + ".start"),
							config.getInt("arenas." + name + ".max"), spawn,
							lobby, end);

			// ��� �� �������, � ���������� �����.

			getArenaManager().getArena(name).setInGame(false);
			getArenaManager().getArena(name).setSpawn(spawn);
			getArenaManager().getArena(name).setEnd(end);
			getArenaManager().getArena(name).setLobby(lobby);
			getArenaManager().getArena(name).setPrefix(
					config.getString("arenas." + name + ".prefix").replace("&",
							"�"));
			getArenaManager().getArena(name).setTime(
					config.getInt("arenas." + name + ".time"));
			getArenaManager().getArena(name).setMax(
					config.getInt("arenas." + name + ".max"));
			getArenaManager().getArena(name).setStart(
					config.getInt("arenas." + name + ".start"));

			// � ���, ��� �� ��� ��������� ����� �� ��������� ��������, �
			// ����������� �������� ������ �� �� ������.

		}
	}

	public static void loadAllArenas(File f) { // �����, ��� ������� ���������
												// ���� ���� �� ������������ �
												// ������ onEnable()

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f); // ����
																			// ������������

		if (config.getConfigurationSection("arenas") != null) { // ���������,
																// ���� �� �
																// �������
																// ������
																// "arenas"

			for (String an : config.getConfigurationSection("arenas")
					.getValues(false).keySet()) { // ���������� �� ����
													// ���������� � ������
													// "arenas"
				loadArena(an, f); // � ��� ��������� ������
			}

		}
	}

	// ����� ������ ������ ��� ������ �������, ������� ����� ������������� �� ��
	// ���������, � � ����������� ��������.

	public static void removeArena(String name, File f) { // ����� ��������
															// �����

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		if (config.getConfigurationSection("arenas." + name) != null) { // ����
																		// ��
																		// ������
																		// arenas.name?
			config.set("arenas." + name, null); // ������ ��� ������ - null
		}

		try {
			config.save(f);
			config.load(f);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void setStart(int i, Arena arena, File f) { // ����� ���
																// ���������
																// ���-��
																// ������� ���
																// ������

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

	public static void setMax(int i, Arena arena, File f) { // ����� ���
															// ��������� ����.
															// �������

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

	public static void setTime(int i, Arena arena, File f) { // ����� ���������
																// ������� ���
																// �����

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

	public static void setSpawn(Location loc, Arena arena, File f) { // �����
																		// ���������
																		// �������
																		// ������

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		String name = arena.getName();

		if (config.getConfigurationSection("arenas." + arena.getName()) != null) { // ����
																					// �����
																					// ������?
																					// �����
																					// ���������
																					// �������
																					// ��
																					// ���,
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

	public static void setLobby(Location loc, Arena arena, File f) { // �����
																		// ���������
																		// �������
																		// �����

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		String name = arena.getName();

		if (config.getConfigurationSection("arenas." + arena.getName()) != null) { // ����
																					// �����
																					// ������?
																					// �����
																					// ���������
																					// �������
																					// ��
																					// ���,
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

	public static void setEnd(Location loc, Arena arena, File f) { // �����
																	// ���������
																	// �������
																	// �����
																	// ����

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		String name = arena.getName();

		if (config.getConfigurationSection("arenas." + arena.getName()) != null) { // ����
																					// �����
																					// ������?
																					// �����
																					// ���������
																					// �������
																					// ��
																					// ���,
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
