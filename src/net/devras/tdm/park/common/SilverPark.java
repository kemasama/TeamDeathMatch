package net.devras.tdm.park.common;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.devras.tdm.Arena;
import net.devras.tdm.park.Park;

public class SilverPark extends Park implements Listener {

	@Override
	public String getName() {
		return "SiliverFish Park";
	}
	@Override
	public String getDescription() {
		return "When you use fishing rod to player,\nit will summon silver fish with a probability\n of 3%.";
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
		return "park.silver";
	}


	@EventHandler
	public void onFish(EntityDamageByEntityEvent event) {
		if (Arena.getInstance().isInGame) {
			if (event.getDamager() instanceof Projectile && event.getEntity() instanceof Player) {
				Projectile pro = (Projectile) event.getDamager();
				if (pro.getShooter() instanceof Player) {
					if ((new Random()).nextInt(100) > 97) {
						Player p = (Player) event.getEntity();
						if (!super.checkPermission((Player) pro.getShooter())) {
							return;
						}

						Silverfish fish = (Silverfish) p.getWorld().spawnEntity(p.getLocation(), EntityType.SILVERFISH);
						fish.setCustomName("§a" + ((Player) pro.getShooter()).getName());
						fish.setTarget(p);
						fish.setMaxHealth(10);
						fish.setHealth(10);
					}
				}
			}
		}
	}

	@Override
	public int getNeedCoin() {
		return 500;
	}
}
