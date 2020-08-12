package net.devras.tdm.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.devras.tdm.Arena;

public class DamageEvent implements Listener {
	@EventHandler
	public void onProjectileDamage(EntityDamageByEntityEvent event) {
		if (Arena.getInstance().isInGame) {
			if (event.getDamager() instanceof Projectile && event.getEntity() instanceof Player && !event.isCancelled()) {
				Projectile pro = (Projectile) event.getDamager();
				if (pro.getShooter() instanceof Player) {
					final Player p = (Player) pro.getShooter();
					final Player e = (Player) event.getEntity();

					if (pro.getType().equals(EntityType.FISHING_HOOK)) {
						p.sendMessage("§c" + e.getDisplayName() + " §eis on §c" + (int) e.getHealth() + " §eHP!");
					}
				}
			}
		}
	}
}
