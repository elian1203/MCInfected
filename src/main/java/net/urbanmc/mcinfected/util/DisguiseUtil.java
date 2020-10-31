package net.urbanmc.mcinfected.util;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class DisguiseUtil {

	static void disguisePlayerAsEntity(Player p, EntityType type) {
		MobDisguise disguise = new MobDisguise(DisguiseType.getType(type));
		disguise.setNotifyBar(DisguiseConfig.NotifyBar.NONE);
		disguise.setSelfDisguiseVisible(false);

		DisguiseAPI.disguiseToAll(p, disguise);
	}

	public static void undisguisePlayer(Player player) {
		DisguiseAPI.undisguiseToAll(player);
	}
}
