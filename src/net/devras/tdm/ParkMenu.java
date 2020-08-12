package net.devras.tdm;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.devras.tdm.park.Park;
import net.devras.tdm.park.ParkManager;
import net.devras.tdm.util.IconMenu;
import net.devras.tdm.util.IconMenu.Row;
import net.devras.tdm.util.IconMenu.onClick;

public class ParkMenu implements Listener {
	public static IconMenu menu;
	public static ParkMenu Instance;

	public ParkMenu() {
		Init();
	}


	public void Init() {
		if (menu == null) {
			menu = new IconMenu("§ePerk Manager", 6, new onClick() {
	
				@Override
				public boolean click(Player p, IconMenu menu, Row row, int slot, ItemStack item) {
	
					if (item != null) {
						if (item.hasItemMeta()) {
							if (item.getItemMeta().hasDisplayName()) {
								String name = item.getItemMeta().getDisplayName();
								if (ParkManager.getParks().containsKey(name)) {
									Park park = ParkManager.getParks().get(name);
	
									if (!park.getName().equals("Park")) {
										if (!p.hasPermission("park.default")) {
											p.sendMessage("§cYou must purchase a Default Perk!");
											return false;
										}
									}
	
									String permission = park.getPermission();
									if (p.hasPermission(permission)) {
										p.sendMessage("§cYou have already this perk!");
									}else {
										if (Game.Money.containsKey(p.getUniqueId())) {
											int point = Game.Money.get(p.getUniqueId());
											if (park.getNeedCoin() <= point) {
	
												point = point - park.getNeedCoin();
												Game.Money.put(p.getUniqueId(), point);
												String command = "lp user " + p.getName() + " permission set " + permission + " true";
	
												Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
												p.sendMessage("§aThank you for purchasing a perk!");
												p.sendMessage("§aYou have " + point + " coins!");
											}else {
												p.sendMessage("§aYou don't have enought coins!");
											}
										}else {
											p.sendMessage("§aYou don't have a profile. Please rejoin this game!");
										}
									}
								}
							}
						}
					}
	
					return false;
				}
	
			});
		}

		int row = 1;
		int pos = 0;
		for (String key : ParkManager.getParks().keySet()) {
			pos++;
			if (pos > 7) {
				row++;
				pos = 1;
			}

			Park park = ParkManager.getParks().get(key);
			//System.out.print(park.getName());

			String[] desc = park.getDescription().split("\n", 9999);

			menu.addButton(menu.getRow(row), pos, makeItem(key), park.getName(), desc);
		}
	}

	public ItemStack makeItem(String key) {

		Park park = ParkManager.getParks().get(key);

		ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(park.getName());
		meta.setLore(new ArrayList<>(Arrays.asList(park.getDescription().split("\n"))));
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);

		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		item.setItemMeta(meta);

		return item;
	}

}
