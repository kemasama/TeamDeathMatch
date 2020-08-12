package net.devras.tdm.event;

import java.util.Set;
import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaStopEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	private Set<UUID> Players;

	public ArenaStopEvent(Set<UUID> set) {
		this.Players = set;
	}

	public boolean hasPlayer(UUID player) {
		return Players.contains(player);
	}
	public Set<UUID> getPlayers(){
		return Players;
	}

}
