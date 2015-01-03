package ru.hilgert.lib.mg.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryUtil {	
	
	private static Map<String, ItemStack[]> invs = new HashMap<>();
	private static Map<String, ItemStack[]> armor = new HashMap<>();
	
	public static void saveInventory(Player p) {
		if(p.getInventory() != null) {
			
			if(invs.containsKey(p.getName()) && armor.containsKey(p.getName())) {
				invs.remove(p.getName());
				armor.remove(p.getName());
			}
			
			invs.put(p.getName(), p.getInventory().getContents());
			armor.put(p.getName(), p.getInventory().getArmorContents());
		}
	}

	@SuppressWarnings("deprecation")
	public static void loadInventory(Player p) {
		if(p != null && invs.get(p.getName()) != null && armor.get(p.getName()) != null && invs.containsKey(p.getName()) && armor.containsKey(p.getName())) {
			p.getInventory().clear();
			
			p.getInventory().setContents(invs.get(p.getName()));
			p.getInventory().setArmorContents(armor.get(p.getName()));
			
			invs.remove(p.getName());
			armor.remove(p.getName());
			
			p.updateInventory();
		}
	}
	
	public static void removePlayerInventory(Player p) {
		if(invs.containsKey(p.getName())) {
			invs.remove(p.getName());
		}
		if(armor.containsKey(p.getName())) {
			armor.remove(p.getName());
		}
	}

	public static void saveInventoryToFile(Inventory inv, File f) {
		
		YamlConfiguration config = new YamlConfiguration();
		
		try {
			config.save(f);
			config.load(f);
		} catch(Exception e1) {
			e1.printStackTrace();
		}
		
		if(inv != null) {
			
			if(inv instanceof PlayerInventory) {
				config.set("inventory.armor", ((PlayerInventory) inv).getArmorContents());
			}
			
			config.set("inventory.content", inv.getContents());
			
			try {
				config.save(f);
				config.load(f);
			} catch(Exception e1) {
				e1.printStackTrace();
			}
 		}
	}
	
	@SuppressWarnings("unchecked")
	public static void loadInventoryFromFile(Inventory inv, File f) {

		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		try {
			config.load(f);
		} catch(Exception e1) {
			e1.printStackTrace();
		}
		
		if(inv != null) {
			
			if(f.exists()) {
			
			if(config.get("inventory.armor") != null) {
				ItemStack[] armor = ((List<ItemStack>) config.get("inventory.armor")).toArray(new ItemStack[0]);
				((PlayerInventory) inv).setArmorContents(armor);
			}
			
			ItemStack[] invs = ((List<ItemStack>) config.get("inventory.content")).toArray(new ItemStack[0]);
			((PlayerInventory) inv).setContents(invs);
			
 		}
		}
	}
	
}
