package net.urbanmc.mcinfected.gson;

import com.google.gson.*;
import net.urbanmc.mcinfected.object.GamePlayer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GamePlayerListSerializer implements JsonSerializer<GamePlayerList>, JsonDeserializer<GamePlayerList> {

	@Override
	public JsonElement serialize(GamePlayerList list, Type type,
	                             JsonSerializationContext context) {
		JsonArray array = new JsonArray();

		Gson gson = new GsonBuilder().registerTypeAdapter(GamePlayer.class, new GamePlayerSerializer()).create();

		for (GamePlayer p : list.getPlayers()) {
			array.add(gson.toJsonTree(p));
		}

		return array;
	}

	@Override
	public GamePlayerList deserialize(JsonElement element, Type type,
	                                  JsonDeserializationContext context) throws
			JsonParseException {
		List<GamePlayer> list = new ArrayList<>();

		Gson gson = new GsonBuilder().registerTypeAdapter(GamePlayer.class, new GamePlayerSerializer()).create();

		for (JsonElement je : element.getAsJsonArray()) {
			list.add(gson.fromJson(je, GamePlayer.class));
		}

		return new GamePlayerList(list);
	}
}
