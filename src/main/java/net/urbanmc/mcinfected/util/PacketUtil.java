package net.urbanmc.mcinfected.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PacketUtil {

	public static void sendActionBar(Player p, String text, String color) {
		String json = "{\"text\":\"" + text + "\", \"color\": \"" + color + "\"}";
		TextComponent comp = new TextComponent(json);

		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, comp);
	}

	public static void sendExplosionParticles(List<Entity> entities, double x, double y, double z) {
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();

		PacketContainer packet = manager.createPacket(PacketType.Play.Server.EXPLOSION);
		packet.getDoubles().write(0, x).write(1, y).write(2, z);
		packet.getFloat().write(0, 0F).write(1, 0F).write(2, 0F).write(3, 1F);
		packet.getIntegers().write(0, 0);
		packet.getBooleans().write(0, true);

		entities.forEach(e -> {
			if (e instanceof Player) {
				try {
					manager.sendServerPacket((Player) e, packet);
				} catch (InvocationTargetException invocationTargetException) {
					invocationTargetException.printStackTrace();
				}
			}
		});
	}

	public static void removePlayersFromList(Player... players) {
		List<EntityPlayer> list = new ArrayList<>();

		for (Player p : players)
			list.add(((CraftPlayer) p).getHandle());


		PacketPlayOutPlayerInfo packet =
				new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, list);

		for (Player p : Bukkit.getOnlinePlayers())
			sendPacket(p, packet);

	}

	public static void removePlayersFromPlayerList(List<Player> removed, Player reciever) {
		List<EntityPlayer> list = new ArrayList<>();

		for (Player p : removed)
			list.add(((CraftPlayer) p).getHandle());

		PacketPlayOutPlayerInfo packet =
				new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, list);

		sendPacket(reciever, packet);
	}

	private static void sendPacket(Player p, Packet packet) {
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
}
