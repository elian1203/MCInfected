package net.urbanmc.mcinfected.runnable;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.ScoreboardManager;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.KitManager;
import net.urbanmc.mcinfected.manager.Messages;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class InfectionStart extends BukkitRunnable {

	private MCInfected plugin;
	private int time;

	InfectionStart(MCInfected plugin) {
		this.plugin = plugin;
		this.time = 60;

		runTaskTimer(plugin, 0, 20);
	}

	@Override
	public void run() {
		if (time == 5 || time == 4 || time == 3 || time == 2 || time == 1) {
			broadcastTime();
		}

		ScoreboardManager.getInstance().minuteCountdown(time);

		if (time == 0) {
			startInfection();
			cancel();
		}

		time--;
	}

	private void broadcastTime() {
		Bukkit.broadcastMessage(Messages.getInstance().getString("infection_starting", time));
	}

	private List<Player> selectMotherZombies() {
		int amount = (Bukkit.getOnlinePlayers().size() / 100) * 20;

		if (Bukkit.getOnlinePlayers().size() < 8) {
			amount = 1;
		}

		Collection<? extends Player> players = Bukkit.getOnlinePlayers();

		Player[] array = new Player[players.size()];
		array = players.toArray(array);

		List<Player> zombies = new ArrayList<>();

		Random r = ThreadLocalRandom.current();

		for (int i = 0; i < amount; i++) {
			int index = r.nextInt(players.size());

			Player p = array[index];

			if (zombies.contains(p)) {
				i--;
				continue;
			}

			zombies.add(p);
		}

		return zombies;
	}

	private void startInfection() {
		List<Player> motherZombies = selectMotherZombies();

		GameManager.getInstance().setGameState(GameManager.GameState.RUNNING);

		for (Player player : motherZombies) {
			GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(player);

			p.setInfected();
			p.setKit(KitManager.getInstance().getMotherKit());

			ItemUtil.equipPlayer(p);

			Bukkit.broadcastMessage(Messages.getInstance().getString("has_become_mother", player.getName()));
		}

		for (Player player : motherZombies) {
			player.sendMessage(Messages.getInstance().getString("you_are_mother"));
		}

		ScoreboardManager.getInstance().createRunningObj();
		new GameTimer(plugin);
	}
}
