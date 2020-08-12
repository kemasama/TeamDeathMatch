package net.devras.tdm.park.common;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.devras.tdm.Game;
import net.devras.tdm.event.ArenaStartEvent;
import net.devras.tdm.park.Park;

public class Knowledge extends Park implements Listener {

	@Override
	public String getName() {
		return "Knowledge";
	}

	@Override
	public void onEnable() {

		Bukkit.getPluginManager().registerEvents(this, getInstance());

		super.onEnable();
	}

	@EventHandler
	public void onStart(ArenaStartEvent event) {
		if (!isActive()) {
			return;
		}

		for (UUID key : event.getPlayers()) {
			Player p = Bukkit.getPlayer(key);
			if (p != null && checkPermission(p)) {
				if (Game.Point.containsKey(key)) {
					int point = Game.Point.get(key);
					point += 5;
					Game.Point.put(key, point);
				}
				int money = 0;
				if (Game.Money.containsKey(key)) {
					money = Game.Money.get(key);
				}
				money += 5;
				Game.Money.put(key, money);
			}
		}
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

			UUID key = p.getUniqueId();
			if (Game.Point.containsKey(key)) {
				int point = Game.Point.get(key);
				point += 5;
				Game.Point.put(key, point);
			}
			int money = 0;
			if (Game.Money.containsKey(key)) {
				money = Game.Money.get(key);
			}
			money += 5;
			Game.Money.put(key, money);

		}
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public String getPermission() {
		return "park.knowledge";
	}

	@Override
	public String getDescription() {
		return "This park give you extra Exp\nand extra coins\nwhen you kill enemy\nand play game.";
	}

	@Override
	public int getNeedCoin() {
		return 1000;
	}

}
