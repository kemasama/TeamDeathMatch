package net.devras.tdm.park.common;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import net.devras.tdm.event.ArenaStartEvent;
import net.devras.tdm.park.Park;

public class DiaChestPlate extends Park implements Listener {

	@Override
	public String getName() {
		return "Diamond Armor";
	}

	@Override
	public void onEnable() {

		Bukkit.getPluginManager().registerEvents(this, getInstance());

		super.onEnable();
	}

	@EventHandler
	public void onStart(ArenaStartEvent event) {
		for (UUID key : event.getPlayers()) {
			Player p = Bukkit.getPlayer(key);
			if (p != null && checkPermission(p)) {
				ItemStack legg = p.getInventory().getLeggings();
				ItemStack boots = p.getInventory().getBoots();
				legg.setType(Material.DIAMOND_LEGGINGS);
				boots.setType(Material.DIAMOND_BOOTS);
				
				p.getInventory().setLeggings(legg);
				p.getInventory().setBoots(boots);
			}
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public String getPermission() {
		return "park.armor.diamond";
	}

	@Override
	public String getDescription() {
		return "When start a game,\nit give you diamond armor.";
	}

	@Override
	public int getNeedCoin() {
		return 10000;
	}

}
