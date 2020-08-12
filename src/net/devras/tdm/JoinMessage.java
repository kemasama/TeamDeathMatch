package net.devras.tdm;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.devras.tdm.util.IconMenu;
import net.devras.tdm.util.IconMenu.Row;
import net.devras.tdm.util.IconMenu.onClick;

public class JoinMessage {
	private static IconMenu menu;
	public static HashMap<Integer, Format> Formats = new HashMap<>();
	private static HashMap<UUID, String> Querys = new HashMap<>();

	static {
		Formats.put(0, new Format("<name> §7joined the game!", -1));
		Formats.put(1, new Format("<name> §1wants to kill!", 100));
		Formats.put(2, new Format("<name> §2plays back on track!", 100));
		Formats.put(3, new Format("<name> §3plays dry out!", 100));
		Formats.put(4, new Format("<name> §4plays base after base!", 100));
		Formats.put(5, new Format("<name> §5plays can't let go!", 100));
		Formats.put(6, new Format("<name> §6plays jumper!", 100));
		Formats.put(7, new Format("<name> §7plays time machine!", 1000));
		Formats.put(8, new Format("<name> §ecome to kill the enemy!", 1000));

		if (MySQL.isConnected()) {
			if (!MySQL.tableExists("joinm")) {
				MySQL.createTable("joinm", "uuid varchar(36) not null primary key, id int not null default 0");
			}
		}

		Init();
	}

	public static void openGui(Player p) {
		menu.open(p);
	}

	public static String getJoinMessage(Player p) {
		int pos = 0;

		try {
			ResultSet res = MySQL.query(String.format("select * from joinm where uuid='%s';", p.getUniqueId().toString()));
			while (res != null && res.next()) {
				pos = res.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return replaceMessage("§b" + Formats.get(pos).getFormat(), p);
	}

	private static String replaceMessage(String from, Player p) {
		return from.replace("<name>", p.getName());
	}

	private static void newJoinMessage(Player p, int id) {
		if (!Formats.containsKey(id)) {
			p.sendMessage("§cNot Found");
			return;
		}

		final UUID key = p.getUniqueId();

		// check have a money yet
		if (!Game.Money.containsKey(key)) {
			return;
		}

		int point = Game.Money.get(key);

		Format format = Formats.get(id);
		if (format.getPoint() > point) {
			p.sendMessage("§cYou dont have enought point!");
			p.sendMessage("§cThis message need point §e" + format.getPoint());
			return;
		}
		
		point = point - format.getPoint();
		Game.Money.put(key, point);

		// add Query
		Querys.put(key, String.format("insert into joinm(uuid, id) values('%s', '%s')"
				+ " on duplicate key update id=values(id);", p.getUniqueId().toString(), id));
		p.sendMessage("§aSuccess add query!");

		Bukkit.getScheduler().runTaskLater(Game.Instance, new Runnable() {
			@Override
			public void run() {
				if (Querys.containsKey(key)) {

					if (Bukkit.getPlayer(key) != null) {
						Bukkit.getPlayer(key).sendMessage("§aSuccess run query!");
						MySQL.update(Querys.get(key));
					}

					Querys.remove(key);
				}
			}
		}, 2L);
	}

	private static void Init() {
		menu = new IconMenu("§eJoin Message", 3, new onClick() {
			@Override
			public boolean click(Player clicker, IconMenu menu, Row row, int slot, ItemStack item) {
				if (row.getRow() == 1) {
					if (slot == 1) {
						newJoinMessage(clicker, 0);
					}
					if (slot == 2) {
						newJoinMessage(clicker, 1);
					}
					if (slot == 3) {
						newJoinMessage(clicker, 3);
					}
					if (slot == 4) {
						newJoinMessage(clicker, 5);
					}
					if (slot == 5) {
						newJoinMessage(clicker, 8);
					}
				}
				return false;
			}
		});

		menu.addButton(menu.getRow(1), 1, new ItemStack(Material.IRON_INGOT), "Disable", "Joined the game!");
		menu.addButton(menu.getRow(1), 2, new ItemStack(Material.DIAMOND), "Wants to kill", "Wants to kill!");
		menu.addButton(menu.getRow(1), 3, new ItemStack(Material.DIAMOND), "Dry Out", "Dry out");
		menu.addButton(menu.getRow(1), 4, new ItemStack(Material.DIAMOND), "Can't let go", "Can't let go");
		menu.addButton(menu.getRow(1), 5, new ItemStack(Material.DIAMOND), "Come to kill", "Come to kill");
	}

	public static class Format{
		private String format;
		private int point;

		public Format(String format, int point) {
			this.format = format;
			this.point = point;
		}

		public String getFormat() {
			return format;
		}

		public int getPoint() {
			return point;
		}
	}

}
