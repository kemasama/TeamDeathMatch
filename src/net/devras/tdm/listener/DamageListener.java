package net.devras.tdm.listener;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.devras.tdm.Arena;
import net.devras.tdm.Game;
import net.devras.tdm.KillLog;
import net.devras.tdm.util.TeamHelper;
import net.devras.tdm.util.Title;

public class DamageListener implements Listener {

	public HashMap<UUID, UUID> protectionHash = new HashMap<>();

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (!Arena.getInstance().isInGame) {
			event.setCancelled(true);
		}else {
			//event.setCancelled(false);
			if (event.getEntity() instanceof Player) {

				// Protection barrier

				Player p = (Player) event.getEntity();
				if (Arena.getInstance().Protection.containsKey(p.getUniqueId()) && Arena.getInstance().Protection.get(p.getUniqueId()) == true) {
					event.setCancelled(true);
					event.setDamage(0);
					//System.out.print(p.getName() + " cancelled damage " + p.getHealth());
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {

		if (!Arena.getInstance().isInGame) {
			event.setCancelled(true);
		}else {
			if (event.isCancelled()) {
				return;
			}

			if (event.getDamager() instanceof Player) {
				// Remove Protection
				Player p = (Player) event.getDamager();
				if (Arena.getInstance().Protection.containsKey(p.getUniqueId())) {
					Arena.getInstance().Protection.remove(p.getUniqueId());
					p.sendMessage("§cYour protection is disabled");
				}
			}
			if (event.getEntity() instanceof Player) {

				// Protection barrier

				Player p = (Player) event.getEntity();
				if (Arena.getInstance().Protection.containsKey(p.getUniqueId()) && Arena.getInstance().Protection.get(p.getUniqueId()) == true) {
					event.setCancelled(true);
					event.setDamage(0);
					//System.out.print(p.getName() + " cancelled damage " + p.getHealth());
				}
			}

		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		final Player p = event.getEntity();

		String newDeathMessage = "§b" + p.getDisplayName() + " §cwas slain!";

		if (p.getKiller() != null) {
			Player k = p.getKiller();
			newDeathMessage = "§b" + p.getDisplayName() + " §cwas slain by §b" + k.getDisplayName();
			if (p.getLastDamageCause() == null || p.getLastDamageCause().getCause() == null) {
				ItemStack item = k.getItemInHand();
				if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
					newDeathMessage += " §cusing [" + item.getItemMeta().getDisplayName() + "]";
				}
			}else {
				switch(p.getLastDamageCause().getCause()) {
				case PROJECTILE:
					ItemStack item = p.getItemInHand();
					newDeathMessage = "§b" + p.getName() + " §cwas shot by §b" + k.getName();
					if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
						newDeathMessage += " §cusing [" + item.getItemMeta().getDisplayName() + "]";
					}
					double distance = p.getLocation().distance(k.getLocation());
					BigDecimal distancebi = new BigDecimal(distance);
					double newdistance = distancebi.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
					newDeathMessage += " §c(§a" + newdistance + " §cblocks)";
					break;
				case FALL:
					newDeathMessage = p.getName() + " §cknocked off a cliff by §b" + k.getName() + "!";
					break;
				case LAVA:
					newDeathMessage = p.getName() + " §ctried to swim in lava to escape §b" + k.getName();
					break;
				case FIRE:
					newDeathMessage = p.getName() + " §cwalked into fire whilst fighting §b" + k.getName();
					break;
				default:
					break;
				}
			}
		}else {
			String messageSuffix = " §cwas slain!";
			switch(p.getLastDamageCause().getCause()) {
			case FALL:
				messageSuffix = " §cfell to their death!";
				break;
			case SUFFOCATION:
				messageSuffix = " §csuffocated in a wall!";
				break;
			case LAVA:
				messageSuffix = " §ctried to swim in lava!";
				break;
			case FIRE:
				messageSuffix = " §cwalked into fire!";
				break;
			case DROWNING:
				messageSuffix = " §cdrowned";
				break;
			case ENTITY_EXPLOSION:
				messageSuffix = " §cexploded";
				break;
			default:
				break;
			}
			newDeathMessage = p.getDisplayName() + messageSuffix;
		}

		event.setDeathMessage("§b" + newDeathMessage);

		event.getDrops().clear();
		event.setDroppedExp(0);
		event.setKeepInventory(true);
		event.setKeepLevel(true);
		event.setNewExp(0);
		event.setNewLevel(0);
		event.setNewTotalExp(0);

		final UUID hash = UUID.randomUUID();

		Arena.getInstance().Protection.put(p.getUniqueId(), true);
		protectionHash.put(p.getUniqueId(), hash);

		int id = 0;

		if (p.getKiller() != null) {

			String team = "";
			id = TeamHelper.getEntry(p.getKiller());
			switch (id) {
			case 1:
				team = "Red";
				break;
			case 2:
				team = "Blue";
				break;
			case 3:
				team = "Green";
				break;
			case 4:
				team = "Yellow";
				break;
			default:
				break;
			}


			if (Arena.getInstance().Kills.get(team) != null) {
				int kill = Arena.getInstance().Kills.get(team);
				kill++;
				Arena.getInstance().Kills.put(team, kill);
			}

			UUID key = p.getKiller().getUniqueId();
			if (Game.Point.containsKey(key)) {
				int point = Game.Point.get(key);
				point += Arena.KILL_POINT * Game.boostPer;
				Game.Point.put(key, point);
				Game.addExp(p, Arena.KILL_POINT);
			}
			int money = 0;
			if (Game.Money.containsKey(key)) {
				money = Game.Money.get(key);
			}
			money += Arena.KILL_MONEY * Game.boostPer;
			Game.Money.put(key, money);

		}

		KillLog.Save(event);

		Bukkit.getScheduler().runTaskLater(Game.Instance, new Runnable() {
			@Override
			public void run() {
				p.spigot().respawn();

				int ids = ((new Random()).nextInt(3)) + 1;

				Arena.getInstance().armor.equip(p);
				Title t = new Title("§cYou died");
				t.send(p);

				if (Game.STATIC_RESPAWN) {
					int
					id = TeamHelper.getEntry(p);
					switch (id) {
					case 1:
						p.teleport(Arena.getInstance().team1);
						break;
					case 2:
						p.teleport(Arena.getInstance().team2);
						break;
					case 3:
						p.teleport(Arena.getInstance().team3);
						break;
					case 4:
						p.teleport(Arena.getInstance().team4);
						break;
					default:
						p.teleport(Arena.getInstance().team1);
						break;
					}

				}else {
					switch (ids) {
					case 1:
						p.teleport(Arena.getInstance().team1);
						break;
					case 2:
						p.teleport(Arena.getInstance().team2);
						break;
					case 3:
						p.teleport(Arena.getInstance().team3);
						break;
					case 4:
						p.teleport(Arena.getInstance().team4);
						break;
					default:
						break;
					}
				}

				p.playSound(p.getLocation(), Sound.CLICK, 1F, 1F);
			}
		}, 1L);

		Bukkit.getScheduler().runTaskLater(Game.Instance, new Runnable() {
			@Override
			public void run() {

				if (protectionHash.containsKey(p.getUniqueId())) {
					if (protectionHash.get(p.getUniqueId()).equals(hash)) {
						if (Arena.getInstance().Protection.containsKey(p.getUniqueId()) && Arena.getInstance().Protection.get(p.getUniqueId())) {
							Arena.getInstance().Protection.remove(p.getUniqueId());
							protectionHash.remove(p.getUniqueId());
							p.sendMessage("§cYour protection is disabled!");
						}
					}
				}

			}
		}, 20L * 10);

	}

	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		event.getDrops().clear();
		event.setDroppedExp(0);
	}
}
