package net.devras.tdm.util;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class ActionBar {
	private PacketPlayOutChat packet;

	public ActionBar(String text) {
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
		this.packet = packet;
	}

	public void sendToPlayer(Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
}
