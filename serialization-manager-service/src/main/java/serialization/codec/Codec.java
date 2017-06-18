package serialization.codec;

import java.lang.reflect.Field;

import com.google.gson.*;

import serialization.SerializableObject;
import serialization.exception.SerializationException;
import serialization.util.ObjectFormatProcessor;

public class Codec {

  private ObjectFormatProcessor objectFormatProcessor;

  public Codec(ObjectFormatProcessor objectFormatProcessor) {
    this.objectFormatProcessor = objectFormatProcessor;
  }

  public JsonObject toJson(SerializableObject objectToSerialize) throws SerializationException {
    Gson gson = new Gson();
    JsonObject json = gson.toJsonTree(objectToSerialize).getAsJsonObject();

    Field idField = objectFormatProcessor.getIdField(objectToSerialize.getClass());
    json.addProperty("id", idField.getName());

    return json;
  }

  public SerializableObject fromJson(JsonObject objectToDeserialize, Class<? extends SerializableObject> clazz) {
    Gson gson = new Gson();
    objectToDeserialize.remove("id");

    return gson.fromJson(objectToDeserialize, clazz);
  }

}
