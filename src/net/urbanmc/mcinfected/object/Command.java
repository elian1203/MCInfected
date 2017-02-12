package net.urbanmc.mcinfected.object;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Command {

	private String name, permission;
	private boolean onlyPlayer;
	private List<String> aliases;

	public Command(String name, String permission, boolean onlyPlayer, List<String> aliases) {
		this.name = name;
		this.permission = permission;
		this.onlyPlayer = onlyPlayer;
		this.aliases = aliases;
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

	public List<String> getAliases() {
		return aliases;
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
