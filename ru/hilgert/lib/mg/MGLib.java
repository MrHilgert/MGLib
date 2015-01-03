package ru.hilgert.lib.mg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

public class MGLib {

	private static final List<String> miniGames = new ArrayList<String>();
	private final HashMap<MiniGame, String> miniGamesImplements = new HashMap<MiniGame, String>();

	public void registerMiniGame(MiniGame mg, String name) {
		miniGames.add(ChatColor.stripColor(name));
		miniGamesImplements.put(mg, ChatColor.stripColor(name));
	}

	public static String getMiniGames() {
		return ChatColor.translateAlternateColorCodes(
				'&',
				"&aПлагины(&f" + miniGames.size() + "&a): "
						+ StringUtils.join(miniGames, ", "));
	}

	@SuppressWarnings("unchecked")
	public HashMap<MiniGame, String> getMiniGamesImplements() {
		return (HashMap<MiniGame, String>) this.miniGamesImplements.clone();
	}

}
