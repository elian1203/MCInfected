package net.urbanmc.mcinfected.util;

import net.minecraft.server.v1_11_R1.*;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class PacketUtil {

	public static void sendActionBar(Player p, String text, String color) {
		String json = "{\"text\":\"" + text + "\", \"color\": \"" + color + "\"}";
		IChatBaseComponent comp = ChatSerializer.a(json);

		PacketPlayOutChat packet = new PacketPlayOutChat(comp, (byte) 2);

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

	private static void sendPacket(Player p, Packet packet) {
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
}
