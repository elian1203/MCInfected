package net.urbanmc.mcinfected.manager;

import net.urbanmc.mcinfected.object.GamePlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InvincibilityManager {

	private static InvincibilityManager instance = new InvincibilityManager();

	private Map<UUID, Integer> map = new ConcurrentHashMap<>();

	public static InvincibilityManager getInstance() {
		return instance;
	}

	public Map<UUID, Integer> getMap() {
		return map;
	}

	public void add(GamePlayer p) {
		map.put(p.getUniqueId(), 3);
	}

	public boolean in(GamePlayer p) {
		return map.containsKey(p.getUniqueId());
	}
}
