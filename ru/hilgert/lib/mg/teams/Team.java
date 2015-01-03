package ru.hilgert.lib.mg.teams;

import java.util.ArrayList;
import java.util.List;
 

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import ru.hilgert.lib.mg.arena.Arena;
 
public class Team extends TeamManager {
 
    private List<String> teamPlayers = new ArrayList<>(); // ������ ������� � �������
    private ChatColor color; // ����, ������� ����� � ������ ��� �������
    private String name; // ��� �������
    private Arena arena; // �����, � ������� ����� ��� �������
 
    public Team(String name, ChatColor color, Arena arena) { // ����� �������� �������, �������� ��� � TeamManager
    	setName(name);
    	setColor(color);
        setArena(arena);
    }
 
    public String getName() { // ���������� ��� �������
        return name;
    }
 
    public void setName(String name) { // ������������� ��� �������
        this.name = name;
    }
 
    public ChatColor getColor() { // ���������� ���� �������... ������ ��� �������, ��� ��� ��.
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
 
    public void sendMessage(String msg) { // ������� ������� ��������� �������
        for(String players : teamPlayers) {
            Bukkit.getPlayer(players).sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }
 
    public boolean isFull() { // ������� �������� - ������ �� �������?
    	return teamPlayers.size() >= (arena.getPlayers().size() / 2);
    }
 
}