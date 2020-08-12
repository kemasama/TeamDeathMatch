package net.devras.tdm.park.common;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.devras.tdm.event.ArenaStartEvent;
import net.devras.tdm.park.Park;

public class StartResistancePark extends Park implements Listener{

	@Override
	public String getName() {
		return "Resistance";
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

	@EventHandler
	public void onStart(ArenaStartEvent event) {
		for (UUID key : event.getPlayers()) {
			Player p = Bukkit.getPlayer(key);
			if (p != null && checkPermission(p)) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 10, 0));
			}
		}
	}

	@Override
	public String getPermission() {
		return "park.resistance";
	}

	@Override
	public String getDescription() {
		return "This park give you resistance 10s when game start.";
	}

	@Override
	public int getNeedCoin() {
		return 1000;
	}

}
