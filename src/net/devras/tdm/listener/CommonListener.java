package net.devras.tdm.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import net.devras.tdm.Arena;
import net.devras.tdm.JoinMessage;
import net.devras.tdm.ParkMenu;
import net.devras.tdm.SpectatePlayer;

public class CommonListener implements Listener {
	@EventHandler
	public void onFood(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onDamageItem(PlayerItemDamageEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void Interact(PlayerInteractEvent event) {
		if (event.getItem() != null) {
			if (event.getItem().getType().equals(Material.NETHER_STAR)) {
				if (!Arena.getInstance().isInGame) {
					Player p = event.getPlayer();
					JoinMessage.openGui(p);
				}
			}
			if (event.getItem().getType().equals(Material.COMPASS)) {
				if (Arena.getInstance().isInGame) {
					Player p = event.getPlayer();
					if (Spectate.isSpec(p)) {
						SpectatePlayer.menu.open(p);
					}
				}
			}
			if (event.getItem().getType().equals(Material.DIAMOND)) {
				if (!Arena.getInstance().isInGame) {
					Player p = event.getPlayer();
					ParkMenu.menu.open(p);
				}
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}

		event.setCancelled(true);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}

		event.setCancelled(true);
	}

}
