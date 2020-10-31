package net.urbanmc.mcinfected.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.urbanmc.mcinfected.protocol.WrapperPlayServerPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PacketUtil {

	public static void sendActionBar(Player p, String text, String color) {
		ChatColor chatColor = null;

		try {
			chatColor = ChatColor.of(color);
		} catch (Exception e) {
			Bukkit.getLogger().warning("[MCInfected] Error parsing chatcolor '" + color + "'!");
			chatColor = ChatColor.WHITE;
		}

		TextComponent comp = new TextComponent(text);
		comp.setColor(chatColor);

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

	private static WrapperPlayServerPlayerInfo createRemovePlayersFromListPacket(Collection<Player> removePlayers) {
		WrapperPlayServerPlayerInfo playerInfoPacket = new WrapperPlayServerPlayerInfo();
		playerInfoPacket.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);

		List<PlayerInfoData> list = new ArrayList<>(removePlayers.size());

		for (Player p : removePlayers) {
			WrappedGameProfile wgp = WrappedGameProfile.fromPlayer(p);
			WrappedChatComponent wcp = WrappedChatComponent.fromText(p.getDisplayName());
			PlayerInfoData pid = new PlayerInfoData(wgp, 0, EnumWrappers.NativeGameMode.SURVIVAL, wcp);
			list.add(pid);
		}

		playerInfoPacket.setData(list);

		return playerInfoPacket;
	}

	public static void removePlayersFromList(Player... players) {
		WrapperPlayServerPlayerInfo playerInfoPacket = createRemovePlayersFromListPacket(Arrays.asList(players));

		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		manager.broadcastServerPacket(playerInfoPacket.getHandle());
	}

	public static void removePlayersFromPlayerList(List<Player> removed, Player reciever) {
		WrapperPlayServerPlayerInfo playerInfoPacket = createRemovePlayersFromListPacket(removed);
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		try {
			manager.sendServerPacket(reciever, playerInfoPacket.getHandle());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static void addPlayersToPlayerList(Collection<Player> addPlayers, Collection<Player> receivers) {
		WrapperPlayServerPlayerInfo playerInfoPacket = new WrapperPlayServerPlayerInfo();
		playerInfoPacket.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);

		List<PlayerInfoData> list = new ArrayList<>(addPlayers.size());

		for (Player p : addPlayers) {
			WrappedGameProfile wgp = WrappedGameProfile.fromPlayer(p);
			WrappedChatComponent wcp = WrappedChatComponent.fromText(p.getDisplayName());
			PlayerInfoData pid = new PlayerInfoData(wgp, 0, EnumWrappers.NativeGameMode.SURVIVAL, wcp);
			list.add(pid);
		}

		playerInfoPacket.setData(list);

		ProtocolManager manager = ProtocolLibrary.getProtocolManager();

		if (receivers == null)
			manager.broadcastServerPacket(playerInfoPacket.getHandle());
		else {
			for (Player player : receivers) {
				try {
					manager.sendServerPacket(player, playerInfoPacket.getHandle());
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
