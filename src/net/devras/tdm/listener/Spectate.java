package net.devras.tdm.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Spectate implements Listener {
	private static HashMap<UUID, Boolean> spec = new HashMap<>();

	public static boolean isSpec(Player p) {
		return spec.containsKey(p.getUniqueId());
	}
	public static void setSpec(Player p) {
		p.setAllowFlight(true);
		p.setFlying(true);
		PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, 9999 * 20, 1, false, false);
		p.addPotionEffect(effect);
		spec.put(p.getUniqueId(), true);
	}
	public static void removeSpec(Player p) {
		spec.remove(p.getUniqueId());
		p.setAllowFlight(false);
		p.setFlying(false);
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}
	}


	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if ((e.getEntity() instanceof Player)) {
			Player damaged = (Player) e.getEntity();
			if (Spectate.isSpec(damaged)) {
				e.setCancelled(true);
			}
		}
		if ((e.getDamager() instanceof Player)) {
			Player damaged = (Player) e.getDamager();
			if (Spectate.isSpec(damaged)) {
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onDamage2(EntityDamageEvent e) {
		if ((e.getEntity() instanceof Player)) {
			if (Spectate.isSpec((Player)e.getEntity())){
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (!p.getGameMode().equals(GameMode.CREATIVE)) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (!p.getGameMode().equals(GameMode.CREATIVE)) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onInv(InventoryOpenEvent e) {
		Player p = (Player) e.getPlayer();
		if (!p.getGameMode().equals(GameMode.CREATIVE)) {
			e.setCancelled(true);
		}
		if (e.getInventory().getType().equals(InventoryType.CHEST)) {
			//e.setCancelled(false);
		}
		if (e.getInventory().getTitle().equalsIgnoreCase("§eJoin Message")) {
			e.setCancelled(false);
		}
		if (e.getInventory().getTitle().startsWith("§e")) {
			e.setCancelled(false);
		}
	}
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (!p.getGameMode().equals(GameMode.CREATIVE)) {
			e.setCancelled(true);
		}

		if (e.getInventory().getTitle().equalsIgnoreCase("§eJoin Message")) {
			e.setCancelled(false);
		}
	}
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (Spectate.isSpec(p)) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (Spectate.isSpec(p)) {
			Spectate.removeSpec(p);
		}
	}
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		Player p = e.getPlayer();
		if (Spectate.isSpec(p)) {
			Spectate.removeSpec(p);
		}
	}
}
