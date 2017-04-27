package net.urbanmc.mcinfected.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketManager {

    private static PacketManager instance = new PacketManager();

    //Defining Reflection variables
    private String version;
    private Constructor<?> packetPlayOutChatConstructor;
    private Class<?> chatSerializer;
    private Method sendPacket;


    public PacketManager() {
        defineReflection();
    }

    private void defineReflection() {
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";

            Class<?> chatComponent = getNMSClass("IChatBaseComponent");
            Class<?> chatClass = getNMSClass("PacketPlayOutChat");
            packetPlayOutChatConstructor = chatClass.getConstructor(chatComponent, byte.class);

            sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", this.getNMSClass("Packet"));

            chatSerializer = getNMSClass("IChatBaseComponent.ChatSerializer");
        } catch (Exception e) {
            System.out.print("[MCInfected] Error with Reflection");
        }
    }

    public static PacketManager getInstance() { return instance; }

    private Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
        String name = "net.minecraft.server." + version + nmsClassString;
        Class<?> nmsClass = Class.forName(name);
        return nmsClass;
    }

    private Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method getHandle = player.getClass().getMethod("getHandle");
        Object nmsPlayer = getHandle.invoke(player);
        Field conField = nmsPlayer.getClass().getField("playerConnection");
        Object con = conField.get(nmsPlayer);
        return con;
    }

    public void sendActionBar(Player player, String text, String color) {
        /*
        Class<?> packetClass = this.getNMSClass("PacketPlayOutWorldParticles");
        Constructor<?> packetConstructor = packetClass.getConstructor(String.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class);
        Object packet = packetConstructor.newInstance(particle, x, y, z, xOffset, yOffset, zOffset, data, amount);
        Method sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", this.getNMSClass("Packet"));
        sendPacket.invoke(this.getConnection(player), packet);
          */

        try {

            Object message = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, "{text: '" + text + "', color: '" + color + "'}");
            Object packetChat = packetPlayOutChatConstructor.newInstance(message, (byte) 2);
            sendPacket.invoke(this.getConnection(player), packetChat);

        } catch (Exception e) {

            System.out.print("[MCInfected] Error sending actionbar message!");

        }


    }



}
