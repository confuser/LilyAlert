package me.confuser.lilyalert;

import java.io.UnsupportedEncodingException;

import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;
import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BarAlertEvents {
	private LilyAlert plugin;
	
	public BarAlertEvents(LilyAlert plugin) {
		this.plugin = plugin;
	}

	@EventListener
	public void onAlert(MessageEvent event) {
		if (!event.getChannel().equals("fcAlert")) { // name of channel
			return;
		}

		String message = "Error";
		try {
			message = event.getMessageAsString();
			Bukkit.broadcastMessage(plugin.getAlertFormat() + message);

			for (Player player : Bukkit.getOnlinePlayers())
				BarAPI.setMessage(player, plugin.getAlertFormat() + message, 20);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
