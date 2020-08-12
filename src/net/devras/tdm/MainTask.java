package net.devras.tdm;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.devras.tdm.listener.Spectate;
import net.devras.tdm.util.ActionBar;
import net.devras.tdm.util.TeamHelper;

public class MainTask implements Runnable {

	private Arena arena;

	public MainTask(Arena arena) {
		this.arena = arena;
	}

	@Override
	public void run() {

		if (arena.isInGame) {
			arena.CRT_GAME_TIME--;
			TeamHelper.setSlot(8, "§eTime§7: §e" + arena.CRT_GAME_TIME);

			if (arena.CRT_GAME_TIME < 1 || arena.Players.size() < 2) {
				arena.Stop();
			}

		}else {
			if (arena.Players.size() > 1) {
				arena.CRT_LOBBY_TIME--;
			}else {
				arena.CRT_LOBBY_TIME = Arena.INT_LOBBY_TIME;
			}

			TeamHelper.setSlot(8, "§eTime§7: §e" + arena.CRT_LOBBY_TIME);

			if (arena.CRT_LOBBY_TIME < 1) {
				arena.Start();
			}

		}

		boolean isAlive = false;
		boolean otherTeam = false;
		int lastTeam = -1;
		int count = 0;
		for (UUID key : arena.Players.keySet()) {
			Player p = Bukkit.getPlayer(key);
			if (p != null) {
				TeamHelper.setHealth(p, (int) p.getHealth());

				int point = Game.Point.containsKey(p.getUniqueId()) ? Game.Point.get(p.getUniqueId()) : 0;
				int money = Game.Money.containsKey(p.getUniqueId()) ? Game.Money.get(p.getUniqueId()) : 0;
				Rank rank = Rank.getRank(point);

				ActionBar bar = new ActionBar("§aPoint §b" + point + "  §b" + rank.getName() + " §aMoney §b" + money);
				bar.sendToPlayer(p);

				if (!Spectate.isSpec(p)) {
					isAlive = true;
					count++;
				}else {
					p.setAllowFlight(true);
					p.setFlying(true);
				}

				int team = TeamHelper.getEntry(p);
				if (lastTeam != -1 && team != lastTeam) {
					otherTeam = true;
				}
				lastTeam = team;

			}
		}

		if (arena.isInGame) {
			if (!isAlive || !otherTeam || count < 2) {
				arena.Stop();
			}
		}


		TeamHelper.setTitle("§a§lTeamDeathMatch");
		/*
		TeamHelper.setSlot(15, "§e");
		TeamHelper.setSlot(14, "§e");
		TeamHelper.setSlot(13, "§e");
		TeamHelper.setSlot(12, "§e");
		TeamHelper.setSlot(11, "§e");
		TeamHelper.setSlot(10, "§e");
		*/
		TeamHelper.setSlot(9, "§7------------");
		TeamHelper.setSlot(7, "§dTeamKills");
		TeamHelper.setSlot(6, " §cRed§7: §e" + arena.Kills.get("Red"));
		TeamHelper.setSlot(5, " §bBlue§7: §e" + arena.Kills.get("Blue"));
		TeamHelper.setSlot(4, " §aGreen§7: §e" + arena.Kills.get("Green"));
		TeamHelper.setSlot(3, " §eYellow§7: §e" + arena.Kills.get("Yellow"));
		TeamHelper.setSlot(2, "§7------------");
		TeamHelper.setSlot(1, "§emc.devras.info");

	}

}
