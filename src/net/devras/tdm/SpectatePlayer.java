package net.devras.tdm;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.devras.tdm.listener.Spectate;
import net.devras.tdm.util.IconMenu;
import net.devras.tdm.util.IconMenu.Row;
import net.devras.tdm.util.IconMenu.onClick;

public class SpectatePlayer implements Listener, Runnable {
	public static IconMenu menu;
	public static SpectatePlayer Instance;

	public SpectatePlayer() {
		Init();
	}


	@Override
	public void run() {
		for (Player p : menu.getViewers()) {
			menu.close(p);
		}

		int row = 1;
		int pos = 1;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!Spectate.isSpec(p)) {

				pos++;
				if (pos > 7) {
					pos = 1;
					row++;
				}

				ItemStack skull = new ItemStack(Material.SKULL_ITEM);
				SkullMeta meta = (SkullMeta) skull.getItemMeta();
				meta.setOwner(p.getPlayerListName());
				meta.setDisplayName(p.getPlayerListName());
				skull.setItemMeta(meta);

				menu.addButton(menu.getRow(row), pos, skull, p.getPlayerListName(), "");

			}
		}

	}

	public void Init() {
		Bukkit.getScheduler().runTaskTimer(Game.Instance, this, 0L, 20L * 10);
		menu = new IconMenu("§ePlayer Teleport", 6, new onClick() {

			@Override
			public boolean click(Player p, IconMenu menu, Row row, int slot, ItemStack item) {

				try {
					if (item.getType().equals(Material.SKULL_ITEM)) {
						SkullMeta meta = (SkullMeta) item.getItemMeta();

						Player pl = Bukkit.getPlayer(meta.getOwner());

						p.teleport(pl);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return false;
			}

		});
	}

}
