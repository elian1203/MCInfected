package net.urbanmc.mcinfected.util;

import net.urbanmc.mcinfected.manager.MapManager;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class VoteUtil {

	public static void addVotes(net.urbanmc.mcinfected.object.Map map, int amount) {
		map.setVotes(map.getVotes() + amount);
	}

	public static String getFormattedSpecific() {
		Map<String, Integer> specificMap = new LinkedHashMap<>();

		for (net.urbanmc.mcinfected.object.Map map : MapManager.getInstance().getSpecific()) {
			specificMap.put(map.getName(), map.getVotes());
		}

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

	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();
		Stream<Map.Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEachOrdered(e -> result.put(e.getKey(), e
				.getValue()));

		return result;
	}
}
