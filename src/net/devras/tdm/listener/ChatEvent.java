package net.devras.tdm.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.devras.tdm.Game;
import net.devras.tdm.Rank;

public class ChatEvent implements Listener{

	@EventHandler(priority=EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent event){
		Player p = event.getPlayer();
		String msg = event.getMessage();

		if (event.isCancelled()) {
			return;
		}

		event.setCancelled(true);

		if (!Game.Point.containsKey(p.getUniqueId())) {
			Game.Point.put(p.getUniqueId(), 0);
		}

		int point = Game.Point.get(p.getUniqueId());
		Rank rank = Rank.getRank(point);

		//String message = String.format("§6%s §b%s§7: §f%s", point, p.getName(), msg);
		String message = String.format("§6%s §e%s§e §b%s§7: §f%s", point, rank.getName(), p.getName(), msg);

		if (Spectate.isSpec(p)) {
			
			for (Player pl : Bukkit.getOnlinePlayers()) {
				if (Spectate.isSpec(pl)) {
					pl.sendMessage(message);
				}
			}
			
			return;
		}
		
		Bukkit.broadcastMessage(message);
	}
}
