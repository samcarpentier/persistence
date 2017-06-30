package serialization.manager.service;

import com.google.gson.JsonObject;

import serialization.manager.service.codec.Codec;
import serialization.manager.service.exception.*;

public class SerializationManager {

  private Codec codec;

  public SerializationManager(Codec codec) {
    this.codec = codec;
  }

  public JsonObject serialize(SerializableObject objectToSerialize) throws SerializationException {
    return codec.toJson(objectToSerialize);
  }

  public SerializableObject deserialize(JsonObject objectToDeserialize, Class<? extends SerializableObject> clazz)
      throws DeserializationException {
    return codec.fromJson(objectToDeserialize, clazz);
  }

}
