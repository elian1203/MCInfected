package net.urbanmc.mcinfected.util;

import org.bukkit.ChatColor;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Messaging {

	private static Messaging instance = new Messaging();

	private ResourceBundle bundle;

	public static Messaging getInstance() {
		return instance;
	}

	private Messaging() {
		loadBundle();
	}

	private void loadBundle() {
		InputStream input = getClass().getResourceAsStream("messages.properties");

		try {
			bundle = new PropertyResourceBundle(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String getString(String key, Object... args) {
		return format(bundle.getString(key), args);
	}

	private String format(String message, Object... args) {
		message = message.replace("{prefix}", bundle.getString("prefix"));
		message = ChatColor.translateAlternateColorCodes('&', message);

		if (args != null) {
			message = MessageFormat.format(message, args);
		}

		return message;
	}
}
