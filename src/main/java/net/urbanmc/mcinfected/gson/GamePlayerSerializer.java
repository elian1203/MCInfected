package net.urbanmc.mcinfected.gson;

import com.google.gson.*;
import net.urbanmc.mcinfected.manager.RankManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.Rank;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GamePlayerSerializer implements JsonSerializer<GamePlayer>, JsonDeserializer<GamePlayer> {

	@Override
	public JsonElement serialize(GamePlayer p, Type type, JsonSerializationContext context) {
		Gson gson = new Gson();
		JsonObject mainObj = new JsonObject();

		mainObj.addProperty("uuid", p.getUniqueId().toString());
		mainObj.addProperty("scores", p.getScores());
		mainObj.addProperty("cookies", p.getCookies());
		mainObj.addProperty("gamesPlayed", p.getGamesPlayed());
		mainObj.addProperty("kills", p.getKills());
		mainObj.addProperty("deaths", p.getDeaths());
		mainObj.addProperty("highestKillStreak", p.getHighestKillStreak());
		mainObj.addProperty("rank", p.getRank().getLevel());

		JsonArray ignoring = new JsonArray();

		for (UUID uuid : p.getIgnoring()) {
			ignoring.add(gson.toJsonTree(uuid));
		}

		mainObj.add("ignoring", ignoring);

		return mainObj;
	}

	@Override
	public GamePlayer deserialize(JsonElement element, Type type,
	                              JsonDeserializationContext context) throws JsonParseException {
		Gson gson = new Gson();
		JsonObject mainObj = (JsonObject) element;

		UUID uuid = UUID.fromString(mainObj.get("uuid").getAsString());
		long scores = mainObj.get("scores").getAsLong(), cookies = mainObj.get("cookies").getAsLong(), gamesPlayed =
				mainObj.get("gamesPlayed").getAsLong();
		int kills = mainObj.get("kills").getAsInt(), deaths = mainObj.get("deaths").getAsInt(), highestKillStreak =
				mainObj.get("highestKillStreak").getAsInt();
		Rank rank = RankManager.getInstance().getRankByLevel(mainObj.get("rank").getAsInt());

		List<UUID> ignoring = new ArrayList<>();

		for (JsonElement je : mainObj.getAsJsonArray("ignoring")) {
			ignoring.add(gson.fromJson(je, UUID.class));
		}

		return new GamePlayer(uuid, scores, cookies, gamesPlayed, kills, deaths, highestKillStreak, rank, ignoring);
	}
}
