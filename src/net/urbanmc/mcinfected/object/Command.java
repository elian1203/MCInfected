package net.urbanmc.mcinfected.object;

import org.bukkit.command.CommandSender;

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
		sender.sendMessage(message);
	}

	public void messagePlayer(GamePlayer p, String message) {
		p.getOnlinePlayer().sendMessage(message);
	}

	public abstract void execute(CommandSender sender, String label, String[] args, GamePlayer p);
}
