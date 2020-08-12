package net.devras.tdm.park;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.devras.tdm.Game;

public class Park implements IPark {

	@Override
	public Game getInstance() {
		return Game.Instance;
	}

	public boolean active;

	@Override
	public String getName() {
		return "Park";
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void onEnable() {
		active = true;

		Bukkit.getPluginManager().addPermission(new Permission(getPermission()));
	}

	@Override
	public void onDisable() {
		active = false;
		
		Bukkit.getPluginManager().removePermission(getPermission());
	}

	@Override
	public String getPermission() {
		return "park.default";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public String getDescription() {
		return "If you want to use some powerful park,\nyou have to purchase this park.\nThis park release level 1 park!";
	}

	@Override
	public int getNeedCoin() {
		return 0;
	}

	public boolean checkPermission(Player key) {
		System.out.println(key.getName() + " / " + getPermission() + " : " + key.hasPermission(getPermission()));
		if (!key.hasPermission("park.default")) {
			return false;
		}

		return key.hasPermission(getPermission());
	}
}
