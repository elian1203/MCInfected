package net.urbanmc.mcinfected.util;

import net.urbanmc.mcinfected.manager.KillStreakManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.KillStreak;

public class KillStreakUtil {

	public static void giveNextKillStreak(GamePlayer p) {
		p.setKillStreak(p.getKillStreak() + 1);

		KillStreak streak = KillStreakManager.getInstance().getKillStreak(p.getKillStreak(), p.isInfected());

		if (streak != null) {
			streak.giveKillStreak(p);
		}
	}
}
