package net.urbanmc.mcinfected.util;

import net.minecraft.server.v1_12_R1.*;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PacketUtil {

	public static void sendActionBar(Player p, String text, String color) {
		String json = "{\"text\":\"" + text + "\", \"color\": \"" + color + "\"}";
		IChatBaseComponent comp = ChatSerializer.a(json);

		PacketPlayOutChat packet = new PacketPlayOutChat(comp, ChatMessageType.GAME_INFO);

		sendPacket(p, packet);
	}

	public static void sendExplosionParticles(List<Entity> entities, float x, float y, float z) {
		PacketPlayOutWorldParticles packet =
				new PacketPlayOutWorldParticles(EnumParticle.EXPLOSION_NORMAL, true, x, y, z, 0, 0, 0, 0, 1);

		entities.forEach(e -> {
			if (e instanceof Player) {
				sendPacket((Player) e, packet);
			}
		});
	}

	public static void removePlayersFromList(Player... players) {
		List<EntityPlayer> list = new ArrayList<>();

		for (Player p : players) {
			list.add(((CraftPlayer) p).getHandle());
		}

		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, list);

		for (Player p : Bukkit.getOnlinePlayers()) {
			sendPacket(p, packet);
		}
	}

	private static void sendPacket(Player p, Packet packet) {
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
}
