package net.urbanmc.mcinfected.util;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class DisguiseUtil {

	public static void disguisePlayerAsEntity(Player p, EntityType type) {
		MobDisguise disguise = new MobDisguise(DisguiseType.getType(type));

		DisguiseAPI.disguiseToAll(p, disguise);
	}

	public static void removePlayerDisguise(Player p) {
		DisguiseAPI.undisguiseToAll(p);
	}
}
