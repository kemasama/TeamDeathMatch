package net.devras.tdm.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.devras.tdm.Game;


public class BoosterListener implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("NL")) {
			return;
		}

		try {
			ByteArrayDataInput in = ByteStreams.newDataInput(message);
			String subChannel = in.readUTF();

			if (subChannel != null && subChannel.equals("booster")) {
				String per = in.readUTF();
				if (per != null) {
					int bPer = Integer.parseInt(per);
					Game.boostPer = bPer;

					if (bPer > 1) {
						Bukkit.broadcastMessage("§f[§6INFO§f] §bActive Booster §6x" + bPer);
					}else {
						Bukkit.broadcastMessage("§f[§6INFO§f] §bDeactive Booster");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
