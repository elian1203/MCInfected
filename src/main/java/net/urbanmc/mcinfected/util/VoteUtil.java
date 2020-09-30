package net.urbanmc.mcinfected.util;

import net.urbanmc.mcinfected.manager.MapManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.Map;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class VoteUtil {
	public static void addVotes(GamePlayer p, net.urbanmc.mcinfected.object.Map map, int amount) {
		map.setVotes(map.getVotes() + amount);

		p.setVoted();

		Bukkit.broadcastMessage(Messages.getInstance()
				                        .getString("player_voted", p.getOnlinePlayer().getName(), map.getName()));
	}

	public static String getFormattedSpecific() {
		List<Object> args = new ArrayList<>();

		for (Map map : MapManager.getInstance().getSpecific()) {
			args.add(map.getName());
			args.add(map.getVotes());
		}

		args.add(MapManager.getInstance().getRandom().getVotes());

		Object[] array = new Object[9];
		array = args.toArray(array);

		return Messages.getInstance().getString("vote_list", array);
	}

	public static net.urbanmc.mcinfected.object.Map getTopVotedMap() {
		java.util.Map<Map, Integer> specificMap = new LinkedHashMap<>();

		for (net.urbanmc.mcinfected.object.Map map : MapManager.getInstance().getSpecific()) {
			specificMap.put(map, map.getVotes());
		}

		net.urbanmc.mcinfected.object.Map random = MapManager.getInstance().getRandom();

		specificMap.put(random, random.getVotes());

		specificMap = sortByValue(specificMap);
		specificMap = removeLowerKeys(specificMap);

		List<Map> list = new ArrayList<>(specificMap.keySet());

		Random r = ThreadLocalRandom.current();

		int index = r.nextInt(list.size());

		Map map = list.get(index);

		if (map.getName().equals("Random")) {
			return MapManager.getInstance().getRealRandom();
		} else {
			return map;
		}
	}

	private static <K, V extends Comparable<? super V>> java.util.Map<K, V> removeLowerKeys(java.util.Map<K, V> map) {
		java.util.Map<K, V> result = new HashMap<>();
		Entry<K, V> firstEntry = map.entrySet().iterator().next();

		map.entrySet().stream().filter(e -> e.getValue() == firstEntry.getValue())
				.forEach(e -> result.put(e.getKey(), e.getValue()));

		return result;
	}

	private static <K, V extends Comparable<? super V>> java.util.Map<K, V> sortByValue(java.util.Map<K, V> map) {
		java.util.Map<K, V> result = new LinkedHashMap<>();
		Stream<Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Collections.reverseOrder(Entry.comparingByValue()))
				.forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

		return result;
	}
}
