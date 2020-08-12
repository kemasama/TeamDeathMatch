package net.devras.tdm;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Armor {
	public Armor() {
	}

	public void equip(Player p) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.getEnderChest().clear();

		PlayerInventory inv = p.getInventory();
		inv.setHelmet(getHelmet());
		inv.setChestplate(getChestPlate());
		inv.setLeggings(getLeggings());
		inv.setBoots(getBoots());
		inv.setItem(0, getSword());
		inv.setItem(1, new ItemStack(Material.FISHING_ROD));
	}

	public ItemStack getSword() {
		ItemStack item = new ItemStack(Material.IRON_SWORD);
		item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eSword");
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getHelmet() {
		ItemStack item = new ItemStack(Material.IRON_HELMET);
		item.addEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§aHelmet");
		item.setItemMeta(meta);

		return item;
	}
	public ItemStack getChestPlate() {
		ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
		item.addEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§aChestplate");
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getLeggings() {
		ItemStack item = new ItemStack(Material.IRON_LEGGINGS);
		item.addEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§aLeggings");
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getBoots() {
		ItemStack item = new ItemStack(Material.IRON_BOOTS);
		item.addEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§aBoots");
		item.setItemMeta(meta);
		return item;
	}
}
