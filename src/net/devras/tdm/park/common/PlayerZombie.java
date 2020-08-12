package net.devras.tdm.park.common;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.devras.tdm.park.Park;

public class PlayerZombie extends Park implements Listener {

	@Override
	public String getName() {
		return "PlayerZombie";
	}

	@Override
	public String getDescription() {
		return "When you die, it will summon a zombie to revenge enemy!";
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
		return "park.zombie";
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


		if ((new Random()).nextInt(100) > 95) {
			try {
				Player p = event.getEntity().getPlayer();
				if (!super.checkPermission(p)) {
					return;
				}

				LivingEntity zombie = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);

				zombie.setCustomName("§c" + p.getName() + "'s Zombie");

				EntityEquipment ee = zombie.getEquipment();

				ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());
				SkullMeta meta = (SkullMeta) skull.getItemMeta();
				meta.setOwner(p.getName());
				skull.setItemMeta(meta);

				ee.setHelmet(skull);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getNeedCoin() {
		return 500;
	}

}
