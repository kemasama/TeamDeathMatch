package net.devras.tdm.park;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

public class ParkManager {

	private static HashMap<String, Park> Parks = new HashMap<>();

	public static void reg(Park park) {
		if (Parks.containsKey(park.getName())) {
			return;
		}

		Parks.put(park.getName(), park);
		park.onEnable();
	}

	public static void unReg(String name) {
		if (Parks.containsKey(name)) {
			Parks.get(name).onDisable();
			Bukkit.getPluginManager().removePermission(new Permission(Parks.get(name).getPermission()));
			Parks.remove(name);
		}
	}
	public static void unReg(Park park) {
		if (Parks.containsKey(park.getName())) {
			park.onDisable();
			Bukkit.getPluginManager().removePermission(new Permission(park.getPermission()));
			Parks.remove(park.getName());
		}
	}

	public static HashMap<String, Park> getParks(){
		return Parks;
	}
}
