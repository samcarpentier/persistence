package util.commons;

import com.google.gson.*;

public class JsonObjectProvider {

  public static JsonObject copy(JsonObject source) {
    Gson gson = new Gson();
    return gson.fromJson(gson.toJson(source), JsonObject.class);
  }

  public static JsonObject fromString(String source) {
    return new Gson().fromJson(source, JsonObject.class);
  }

}
