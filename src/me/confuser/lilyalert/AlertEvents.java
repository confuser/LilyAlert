package me.confuser.lilyalert;

import java.io.UnsupportedEncodingException;

import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;

import org.bukkit.Bukkit;

public class AlertEvents {
	private LilyAlert plugin;

	public AlertEvents(LilyAlert plugin) {
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

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
