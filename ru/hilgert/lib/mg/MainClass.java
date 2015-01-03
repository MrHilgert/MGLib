package ru.hilgert.lib.mg;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import ru.hilgert.lib.mg.events.PlayerEventCaller;

public class MainClass extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("MGLib enabled");
		Bukkit.getPluginManager().registerEvents(new PlayerEventCaller(this),
				this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		sender.sendMessage(MGLib.getMiniGames());
		return false;
	}

}
