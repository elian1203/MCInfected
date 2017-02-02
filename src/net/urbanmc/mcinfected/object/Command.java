package net.urbanmc.mcinfected.object;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Command {

	private String name, permission;
	private boolean onlyPlayer;

	public Command(String name, String permission, boolean onlyPlayer) {
		this.name = name;
		this.permission = permission;
		this.onlyPlayer = onlyPlayer;
	}

	public String getName() {
		return name;
	}

	public String getPermission() {
		return permission;
	}

	public boolean isOnlyPlayer() {
		return onlyPlayer;
	}

	public void messageSender(CommandSender sender, String message) {
		if (!(sender instanceof Player)) {
			message = ChatColor.stripColor(message);
		}

		sender.sendMessage(message);
	}

	public void messagePlayer(GamePlayer p, String message) {
		p.getOnlinePlayer().sendMessage(message);
	}

	public String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public abstract void execute(CommandSender sender, String label, String[] args, GamePlayer p);
}
