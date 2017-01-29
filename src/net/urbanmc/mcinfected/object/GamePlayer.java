package net.urbanmc.mcinfected.object;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.UUID;

public class GamePlayer {

	private UUID uuid;
	private long cookies, gamesPlayed;
	private int kills, deaths, killStreak;
	private Rank rank;
	private Kit kit;
	private boolean voted = false;

	public GamePlayer(UUID uuid, long cookies, long gamesPlayed, int kills, int deaths, Rank rank) {
		this.uuid = uuid;
		this.cookies = cookies;
		this.gamesPlayed = gamesPlayed;
		this.kills = kills;
		this.deaths = deaths;
		this.rank = rank;
	}

	public UUID getUniqueId() {
		return uuid;
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

	public Rank getRank() {
		return rank;
	}

	public Kit getKit() {
		return kit;
	}

	public String getKDR() {
		double kdr = kills / deaths;

		DecimalFormat df = new DecimalFormat("##.##");

		return df.format(kdr);
	}

	public void setCookies(long cookies) {
		this.cookies = cookies;
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

	public void setRank(Rank rank) {
		this.rank = rank;
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

	public void setVoted(boolean voted) {
		this.voted = voted;
	}

	public Player getOnlinePlayer() {
		return Bukkit.getPlayer(uuid);
	}
}
