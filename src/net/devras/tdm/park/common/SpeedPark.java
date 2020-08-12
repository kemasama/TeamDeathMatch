package net.devras.tdm.park.common;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.devras.tdm.park.Park;

public class SpeedPark extends Park implements Listener {

	@Override
	public String getName() {
		return "Speed";
	}

	@Override
	public String getDescription() {
		return "When you kill enemy,\nit will give you speed";
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
		return "park.effect.speed";
	}


	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {

		if (!isActive()) {
			return;
		}

		if (event.getEntity().getKiller() != null) {
			Player p = event.getEntity().getKiller();
			if (!super.checkPermission(p)) {
				return;
			}

			if ((new Random()).nextInt(100) > 95) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 0));
			}
		}
	}

	@Override
	public int getNeedCoin() {
		return 10000;
	}

}
