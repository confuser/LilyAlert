package me.confuser.lilyalert;

import java.io.UnsupportedEncodingException;
import java.util.List;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

		if (Bukkit.getServer().getPluginManager().getPlugin("BarAPI") != null)
			lilyPad.registerEvents(new BarAlertEvents(this));
		else
			lilyPad.registerEvents(new AlertEvents(this));
	}

	// Reload
	public void configReload() {
		reloadConfig();

		servers = getConfig().getStringList("serverUsernames");
		alertFormat = colorize(getConfig().getString("format"));
	}

	public void request(String message) {
		Connect c = getBukkitConnect();
		MessageRequest request = null;

		try {
			request = new MessageRequest(servers, "alert", message);
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
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	public String getAlertFormat() {
		return alertFormat;
	}
}
