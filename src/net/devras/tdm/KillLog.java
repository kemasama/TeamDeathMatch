package net.devras.tdm;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillLog {

	public static void Init() {
		if (!MySQL.tableExists("log")) {
			MySQL.createTable("log", "id int not null auto_increment primary key, "
					+ "player varchar(36) not null, "
					+ "name varchar(36) not null, "
					+ "killer varchar(36) not null, "
					+ "kname varchar(36) not null, "
					+ "date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP");
		}
	}

	public static void Save(PlayerDeathEvent event) {
		Player entity = event.getEntity();
		if (entity.getKiller() != null) {
			Player killer = entity.getKiller();
			final String sql = String.format("insert into log(player, name, killer, kname) values('%s', '%s', '%s', '%s');", entity.getUniqueId().toString(), entity.getName(), killer.getUniqueId().toString(), killer.getName());
			new Thread(new Runnable() {
				@Override
				public void run() {
					MySQL.update(sql);
				}
			}).start();
		}
	}
}
