package serialization.codec;

import java.lang.reflect.Type;

import com.google.gson.*;

import serialization.SerializableObject;

public abstract class GenericCodec<T extends SerializableObject> implements JsonSerializer<T>, JsonDeserializer<T> {

	public abstract JsonElement serialize(T object, Type type, JsonSerializationContext context);

	public abstract T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context);

}
