package net.urbanmc.mcinfected.object;

import net.urbanmc.mcinfected.manager.KitManager;
import net.urbanmc.mcinfected.runnable.LastAttacker;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class GamePlayer {

	// Persistent Variables
	private UUID uuid;
	private long scores, cookies, gamesPlayed;
	private int kills, deaths, highestKillStreak;
	private List<UUID> ignoring;
	private Rank rank;

	// Non-Persistent Variables
	private int killStreak;
	private Kit kit;
	private boolean voted = false, sneaking = false, infected = false, motherZombie = false;
	private GamePlayer lastAttacker, lastMessenger;
	private LastAttacker lastAttackerRunnable;

	public GamePlayer(UUID uuid, long scores, long cookies, long gamesPlayed, int kills, int deaths,
	                  int highestKillStreak, Rank rank, List<UUID> ignoring) {
		this.uuid = uuid;
		this.scores = scores;
		this.cookies = cookies;
		this.gamesPlayed = gamesPlayed;
		this.kills = kills;
		this.deaths = deaths;
		this.highestKillStreak = highestKillStreak;
		this.rank = rank;
		this.ignoring = ignoring;
	}

	public UUID getUniqueId() {
		return uuid;
	}

	public long getScores() {
		return scores;
	}

	public long getCookies() {
		return cookies;
	}

	public void setCookies(long cookies) {
		this.cookies = cookies;
	}

	public long getGamesPlayed() {
		return gamesPlayed;
	}

	public void setGamesPlayed(long gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getHighestKillStreak() {
		return highestKillStreak;
	}

	public void setHighestKillStreak(int hightestKillStreak) {
		this.highestKillStreak = hightestKillStreak;
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public List<UUID> getIgnoring() {
		return ignoring;
	}

	public boolean isIgnoring(UUID uuid) {
		return ignoring.contains(uuid);
	}

	public Kit getKit() {
		return kit;
	}

	public void setKit(Kit kit) {
		this.kit = kit;
	}

	public String getKDR() {
		double kdr;

		if (deaths != 0)
			kdr = (double) kills / deaths;
		else
			kdr = kills;

		DecimalFormat df = new DecimalFormat("##.##");

		return df.format(kdr);
	}

	public void giveScores(long scores) {
		this.scores += scores;
	}

	public void giveCookies(long cookies) {
		this.cookies += cookies;
	}

	public void addToIgnoring(UUID uuid) {
		ignoring.add(uuid);
	}

	public void removeFromIgnoring(UUID uuid) {
		ignoring.remove(uuid);
	}

	public int getKillStreak() {
		return killStreak;
	}

	public void setKillStreak(int killStreak) {
		this.killStreak = killStreak;
	}

	public boolean hasVoted() {
		return voted;
	}

	public void setVoted() {
		voted = true;
	}

	public boolean isSneaking() {
		return sneaking;
	}

	public void setSneaking(boolean sneaking) {
		this.sneaking = sneaking;
	}

	public Player getOnlinePlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(uuid);
	}

	public boolean isInfected() {
		return infected;
	}

	public void setInfected() {
		infected = true;

		if (kit == null) {
			kit = KitManager.getInstance().getZombieKit();
		}
	}

	public boolean isMotherZombie() {
		return motherZombie;
	}

	public void setMotherZombie() {
		infected = true;
		motherZombie = true;

		if (kit == null) {
			kit = KitManager.getInstance().getMotherKit();
		}
	}

	public GamePlayer getLastAttacker() {
		return lastAttacker;
	}

	public void setLastAttacker(GamePlayer lastAttacker) {
		this.lastAttacker = lastAttacker;

		if (lastAttackerRunnable != null)
			lastAttackerRunnable.cancel();

		lastAttackerRunnable = new LastAttacker(this);
	}

	public GamePlayer getLastMessenger() {
		return lastMessenger;
	}

	public void setLastMessenger(GamePlayer lastMessenger) {
		this.lastMessenger = lastMessenger;
	}

	public void setLastAttackerRunnable(LastAttacker runnable) {
		this.lastAttackerRunnable = runnable;
	}

	public void resetGame() {
		// Reset game variables
		this.killStreak = 0;
		this.infected = false;
		this.motherZombie = false;
		this.kit = null;

		if (lastAttackerRunnable != null)
			lastAttackerRunnable.cancel();

		lastAttacker = null;
		lastAttackerRunnable = null;

		// Reset map vote
		this.voted = false;
	}

}
