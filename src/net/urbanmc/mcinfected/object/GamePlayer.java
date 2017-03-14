package net.urbanmc.mcinfected.object;

import net.urbanmc.mcinfected.manager.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class GamePlayer {

	private UUID uuid;
	private long scores, cookies, gamesPlayed;
	private int kills, deaths, highestKillStreak, killStreak;
	private List<UUID> ignoring;
	private Rank rank;
	private Kit kit;
	private boolean voted = false;
	private boolean sneaking;
	private boolean infected;
	private GamePlayer lastAttacker, lastMessager;

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

	public long getGamesPlayed() {
		return gamesPlayed;
	}

	public int getKills() {
		return kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public int getHighestKillStreak() {
		return highestKillStreak;
	}

	public Rank getRank() {
		return rank;
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

	public String getKDR() {

		double kdr;
		if (deaths != 0)
			kdr = kills / deaths;
		else
			kdr = kills;

		DecimalFormat df = new DecimalFormat("##.##");

		return df.format(kdr);
	}

	public void setScores(long scores) {
		this.scores = scores;
	}

	public void giveScores(long scores) {
		this.scores += scores;
	}

	public void setCookies(long cookies) {
		this.cookies = cookies;
	}

	public void giveCookies(long cookies) {
		this.cookies += cookies;
	}

	public void setGamesPlayed(long gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public void setHighestKillStreak(int hightestKillStreak) {
		this.highestKillStreak = hightestKillStreak;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public void addToIgnoring(UUID uuid) {
		ignoring.add(uuid);
	}

	public void removeFromIgnoring(UUID uuid) {
		ignoring.remove(uuid);
	}

	public void setKit(Kit kit) {
		this.kit = kit;
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

		kit = KitManager.getInstance().getZombieKit();
	}

	public GamePlayer getLastAttacker() {
		return lastAttacker;
	}

	public void setLastAttacker(GamePlayer lastAttacker) {
		this.lastAttacker = lastAttacker;
	}

	public GamePlayer getLastMessager() {
		return lastMessager;
	}

	public void setLastMessager(GamePlayer lastMessager) {
		this.lastMessager = lastMessager;
	}
}
