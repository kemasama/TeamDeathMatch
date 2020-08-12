package net.devras.tdm.listener;

import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.devras.tdm.Arena;
import net.devras.tdm.Game;
import net.devras.tdm.JoinMessage;
import net.devras.tdm.MySQL;
import net.devras.tdm.util.TeamHelper;

public class JoinListener implements Listener {
	@EventHandler
	public void onPost(PlayerLoginEvent event) {
		if (Arena.getInstance().isInGame) {
			//event.disallow(Result.KICK_FULL, "Game is already starting...");
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		Arena.getInstance().Players.put(p.getUniqueId(), true);
		if (!Arena.getInstance().isInGame) {
			Arena.getInstance().addPlayer(p);
			TeamHelper.setHealth(p, (int) p.getHealth());
			Arena.getInstance().armor.equip(p);
			
			ItemStack perk = new ItemStack(Material.DIAMOND);
			ItemMeta perkMeta = perk.getItemMeta();
			perkMeta.setDisplayName("§ePerk");
			perk.setItemMeta(perkMeta);
			
			ItemStack join = new ItemStack(Material.NETHER_STAR);
			ItemMeta joinMeta = join.getItemMeta();
			joinMeta.setDisplayName("§eJoin Message");
			join.setItemMeta(joinMeta);
			
			p.getInventory().setItem(7, perk);
			p.getInventory().setItem(8, join);
		}

		event.setJoinMessage(JoinMessage.getJoinMessage(p));

		try {
			p.teleport(Arena.getInstance().lobby);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Game.Point.put(p.getUniqueId(), 0);
		Game.Money.put(p.getUniqueId(), 0);

		p.setMaxHealth(20.0);
		p.setHealth(p.getMaxHealth());
		p.setGameMode(GameMode.ADVENTURE);
		p.setAllowFlight(false);
		p.setFlying(false);

		if (MySQL.isConnected()) {
			try {
				ResultSet res = MySQL.query(String.format("select * from point where uuid='%s';", p.getUniqueId().toString()));
				while (res != null && res.next()) {
					Game.Point.put(p.getUniqueId(), res.getInt("point"));
				}

				ResultSet res2 = MySQL.query(String.format("select * from money where uuid='%s';", p.getUniqueId().toString()));
				while (res2 != null && res2.next()) {
					Game.Money.put(p.getUniqueId(), res2.getInt("point"));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (Arena.getInstance().isInGame) {
			p.teleport(Arena.getInstance().team1);
			final Player pl = p.getPlayer();
			pl.getInventory().setArmorContents(null);
			pl.getInventory().clear();
			Bukkit.getScheduler().runTaskLater(Game.Instance, new Runnable() {
				@Override
				public void run() {
					Spectate.setSpec(pl);
					Game.hidePlayer(pl);
					pl.setAllowFlight(true);
					pl.setFlying(true);
					pl.setGameMode(GameMode.ADVENTURE);
					pl.setPlayerListName("§7[SPEC] §f" + pl.getName());

					pl.getInventory().setItem(8, new ItemStack(Material.COMPASS));
				}
			}, 20L * 1);

			event.setJoinMessage("");
		}else {
			Game.showPlayer(p);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (Arena.getInstance().Players.containsKey(p.getUniqueId())) {
			Arena.getInstance().Players.remove(p.getUniqueId());
		}

		Arena.getInstance().removePlayer(p);

		event.setQuitMessage("");

		if (MySQL.isConnected()) {
			try {
				//ResultSet res = Game.Instance.SQL.query(String.format("select * from point where uuid='%s';", p.getUniqueId().toString()));
				MySQL.update(String.format("insert into point(uuid, point) values('%s', '%s')"
						+ " on duplicate key update"
						+ " point=values(point);", p.getUniqueId().toString(), Game.Point.get(p.getUniqueId())));
				MySQL.update(String.format("insert into money(uuid, point) values('%s', '%s')"
						+ " on duplicate key update"
						+ " point=values(point);", p.getUniqueId().toString(), Game.Money.get(p.getUniqueId())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (Spectate.isSpec(p)) {
			Spectate.removeSpec(p);
		}

	}
}
