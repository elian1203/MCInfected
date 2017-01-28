package net.urbanmc.mcinfected.util;

import net.urbanmc.mcinfected.manager.MapManager;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class VoteUtil {

	public static void addVotes(net.urbanmc.mcinfected.object.Map map, int amount) {
		map.setVotes(map.getVotes() + amount);
	}

	/**
	 * @return A list with the top 5 maps
	 */
	public static Map<net.urbanmc.mcinfected.object.Map, Integer> getTopVotedMaps() {
		Map<net.urbanmc.mcinfected.object.Map, Integer> map = new LinkedHashMap<net.urbanmc.mcinfected.object.Map, Integer>();

		for (net.urbanmc.mcinfected.object.Map gameMap : MapManager.getInstance().getMaps()) {
			map.put(gameMap, gameMap.getVotes());
		}

		return sortByValue(map);
	}

	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<K, V>();
		Stream<Map.Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

		return result;
	}
}
