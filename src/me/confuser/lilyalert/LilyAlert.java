package me.confuser.lilyalert;

import java.io.UnsupportedEncodingException;
import java.util.List;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.MessageEvent;
import lilypad.client.connect.api.MessageEventListener;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class LilyAlert extends JavaPlugin implements Listener {
	private Connect lilyPad;
	public List<String> servers;
	private String alertFormat = "";

	public void onEnable() {
		// Load config
		getConfig().options().copyDefaults(true);
		saveConfig();

		configReload();

		lilyPad = getBukkitConnect();

		getCommand("alert").setExecutor(new AlertCommand(this));

		lilyPad.registerMessageEventListener(new MessageEventListener() {
			public void onMessage(Connect connect, MessageEvent me) {
				if (!me.getChannel().equals("alert")) { // name of channel
					return;
				}

				String message = "Error";
				try {
					message = me.getMessageAsString();
					Bukkit.broadcastMessage(alertFormat + message);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Reload
	public void configReload() {
		reloadConfig();

		servers = getConfig().getStringList("serverUsernames");
		alertFormat = colorize(getConfig().getString("format"));
	}

	public void request(String serverName, String message) {
		Connect c = getBukkitConnect();
		MessageRequest request = null;

		try {
			request = new MessageRequest(serverName, "alert", message);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			c.request(request);
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}

	public Connect getBukkitConnect() {
		return (Connect) getServer().getServicesManager().getRegistration(Connect.class).getProvider();
	}

	public String colorize(String string) {
		return string.replaceAll("(?i)&([a-k0-9])", "\u00A7$1");
	}
}
