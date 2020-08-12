package net.devras.tdm.park.common;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.devras.tdm.park.Park;

public class GapplePark extends Park implements Listener {
	@Override
	public String getName() {
		return "Gapple Park";
	}

	@Override
	public String getDescription() {
		return "When you kill enemy\nit will give you gapple with a probability\n of 3%.";
	}

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, getInstance());
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public String getPermission() {
		return "park.gapple";
	}

	@EventHandler
	public void onKill(PlayerDeathEvent event) {

		if (!isActive()) {
			return;
		}

		Player player = event.getEntity();
		if (player.getKiller() == null) {
			return;
		}


		Player killer = player.getKiller();

		if (!super.checkPermission(killer)) {
			return;
		}

		Random r = new Random();
		if (r.nextInt(100) > 97) {
			killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
		}

	}

	@Override
	public int getNeedCoin() {
		return 1000;
	}
}
