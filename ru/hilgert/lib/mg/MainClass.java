package ru.hilgert.lib.mg;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import ru.hilgert.lib.mg.events.PlayerEventCaller;

public class MainClass extends JavaPlugin {

	private static MainClass inst;
	
	@Override
	public void onEnable() {
		inst = this;
		getLogger().info("MGLib enabled");
		Bukkit.getPluginManager().registerEvents(new PlayerEventCaller(this),
				this);
	}
	
	public static MainClass getInstance(){
		return inst;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		sender.sendMessage(MGLib.getMiniGames());
		return false;
	}

}
