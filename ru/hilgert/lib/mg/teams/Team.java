package ru.hilgert.lib.mg.teams;

import java.util.ArrayList;
import java.util.List;
 

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import ru.hilgert.lib.mg.arena.Arena;
 
public class Team extends TeamManager {
 
    private List<String> teamPlayers = new ArrayList<>(); // Список игроков в команде
    private ChatColor color; // Цвет, который будет у игрока над головой
    private String name; // Имя команды
    private Arena arena; // Арена, в которой будет эта команда
 
    public Team(String name, ChatColor color, Arena arena) { // Метод создания команды, затронем его в TeamManager
    	setName(name);
    	setColor(color);
        setArena(arena);
    }
 
    public String getName() { // Возвращает имя команды
        return name;
    }
 
    public void setName(String name) { // Устанавливает имя команды
        this.name = name;
    }
 
    public ChatColor getColor() { // Возвращает цвет команды... Дальше все понятно, все так же.
        return color;
    }
 
    public void setColor(ChatColor color) {
        this.color = color;
    }
 
    public List<String> getTeamPlayers() {
        return teamPlayers;
    }
 
    public void setTeamPlayers(List<String> teamPlayers) {
        this.teamPlayers = teamPlayers;
    }
 
    public Arena getArena() {
        return arena;
    }
 
    public void setArena(Arena arena) {
        this.arena = arena;
    }
 
    public void sendMessage(String msg) { // Утилита отсылки сообщения команде
        for(String players : teamPlayers) {
            Bukkit.getPlayer(players).sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }
 
    public boolean isFull() { // Утилита проверки - полная ли команда?
    	return teamPlayers.size() >= (arena.getPlayers().size() / 2);
    }
 
}