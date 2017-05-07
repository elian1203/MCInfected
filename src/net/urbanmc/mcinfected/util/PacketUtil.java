package net.urbanmc.mcinfected.util;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_11_R1.*;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

    public static void sendPlayerList(Player removePlayer, Player sendPlayer) {

        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((EntityPlayer) removePlayer));
        /*try {
            Field a = packet.getClass().getDeclaredField("a");
            a.setAccessible(true);
            a.set(packet, PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
            Field b = packet.getClass().getDeclaredField("b");
            b.setAccessible(true);
            PacketPlayOutPlayerInfo.PlayerInfoData data = new PacketPlayOutPlayerInfo().new PlayerInfoData(((CraftPlayer) removePlayer).getProfile(), 0, EnumGamemode.ADVENTURE, ChatSerializer.a(removePlayer.getName()));
            b.set(packet, Arrays.asList(data));
            sendPacket(sendPlayer, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        sendPacket(sendPlayer, packet);
    }

    private static void sendPacket(Player p, Packet packet) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
}
