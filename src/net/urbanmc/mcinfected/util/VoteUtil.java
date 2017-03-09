package net.urbanmc.mcinfected.util;

import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.*;
import java.util.stream.Stream;

public class VoteUtil {

	public static void addVotes(GamePlayer p, net.urbanmc.mcinfected.object.Map map, int amount) {
		map.setVotes(map.getVotes() + amount);

		p.setVoted();

		Bukkit.broadcastMessage(Messages.getInstance().getString("player_voted",
		                                                         p.getOnlinePlayer().getName(),
		                                                         map.getName()));
	}

	public static String getFormattedSpecific() {
		Map<String, Integer> specificMap = new LinkedHashMap<>();

		for (net.urbanmc.mcinfected.object.Map map : MapManager.getInstance().getSpecific()) {
			specificMap.put(map.getName(), map.getVotes());
		}

		net.urbanmc.mcinfected.object.Map random = MapManager.getInstance().getRandom();

		specificMap.put(random.getName(), random.getVotes());

		specificMap = sortByValue(specificMap);

		StringBuilder builder = new StringBuilder();

		Iterator<Map.Entry<String, Integer>> itr = specificMap.entrySet().iterator();

		for (int i = 0; i < specificMap.size(); i++) {
			Map.Entry<String, Integer> entry = itr.next();

			String s = ChatColor.GOLD + Integer.toString(i) + ". " + ChatColor.AQUA + entry.getKey() + ": " +
					ChatColor.GREEN + entry.getValue() + "\n";

			builder.append(s);
		}

		builder.setLength(builder.length() - 1);

		return builder.toString();
	}

	public static net.urbanmc.mcinfected.object.Map getTopVotedMap() {
		Map<net.urbanmc.mcinfected.object.Map, Integer> specificMap = new LinkedHashMap<>();

		for (net.urbanmc.mcinfected.object.Map map : MapManager.getInstance().getSpecific()) {
			specificMap.put(map, map.getVotes());
		}

		net.urbanmc.mcinfected.object.Map random = MapManager.getInstance().getRandom();

		specificMap.put(random, random.getVotes());

		specificMap = sortByValue(specificMap);

		List<net.urbanmc.mcinfected.object.Map> list = new ArrayList<>(specificMap.keySet());

		return list.get(0);
	}

	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();
		Stream<Map.Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEachOrdered(e -> result.put(e.getKey(),
		                                                                                                 e.getValue()));

		return result;
	}
}
