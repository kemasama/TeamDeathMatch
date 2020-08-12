package net.devras.tdm.park.common;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.devras.tdm.park.Park;

public class UpgradeArmor extends Park implements Listener {

	@Override
	public String getName() {
		return "Armor Plus";
	}

	@Override
	public void onEnable() {

		Bukkit.getPluginManager().registerEvents(this, getInstance());

		super.onEnable();
	}

	@EventHandler
	public void onKill(PlayerDeathEvent event) {
		if (!isActive()) {
			return;
		}

		if (event.getEntity().getKiller() != null) {
			Player p = event.getEntity().getKiller();
			if (!checkPermission(p)) {
				return;
			}

			if ((new Random()).nextInt(100) < 2) {
				ItemStack item = p.getInventory().getArmorContents()[(new Random()).nextInt(p.getInventory().getArmorContents().length)];
				//p.getInventory().getChestplate();
				try {
					int lv = item.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
					lv++;
					if (lv > 3) {
						lv = 3;
					}
					item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, lv);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}


	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public String getPermission() {
		return "park.armor";
	}

	@Override
	public String getDescription() {
		return "When you kill enemy,\nit give you protection\nwith probably 1%.";
	}

	@Override
	public int getNeedCoin() {
		return 10000;
	}

}
