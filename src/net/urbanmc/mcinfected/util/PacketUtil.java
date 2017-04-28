package net.urbanmc.mcinfected.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class PacketUtil {

	//Defining Reflection variables
	private static String version;
	private static Constructor<?> packetPlayOutChatConstructor;
	private static Class<?> chatSerializer;
	private static Method sendPacket;

	static {
		defineReflection();
	}

	private static void defineReflection() {
		try {
			version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";

			Class<?> chatComponent = getNMSClass("IChatBaseComponent");
			Class<?> chatClass = getNMSClass("PacketPlayOutChat");
			packetPlayOutChatConstructor = chatClass.getConstructor(chatComponent, byte.class);

			sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));

			chatSerializer = getNMSClass("IChatBaseComponent.ChatSerializer");
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.SEVERE, "[MCInfected] Error with Reflection");
		}
	}

	private static Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
		String name = "net.minecraft.server." + version + nmsClassString;
		Class<?> nmsClass = Class.forName(name);
		return nmsClass;
	}

	private static Object getConnection(
			Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method getHandle = player.getClass().getMethod("getHandle");
		Object nmsPlayer = getHandle.invoke(player);
		Field conField = nmsPlayer.getClass().getField("playerConnection");
		return conField.get(nmsPlayer);
	}

	public static void sendActionBar(Player player, String text, String color) {
		try {
			Object message = chatSerializer.getMethod("a", String.class)
					.invoke(chatSerializer, "{\"text\": \"" + text + "\", \"color\": \"" + color + "\"}");
			Object packetChat = packetPlayOutChatConstructor.newInstance(message, (byte) 2);

			sendPacket.invoke(getConnection(player), packetChat);
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.SEVERE, "[MCInfected] Error sending ActionBar message!");
		}
	}
}
