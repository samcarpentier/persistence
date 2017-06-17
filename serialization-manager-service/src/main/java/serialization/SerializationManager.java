package serialization;

import com.google.gson.JsonElement;

import serialization.codec.*;

public class SerializationManager {

	private CodecRegistrar codecRegistrar;

	public SerializationManager(CodecRegistrar codecRegistrar) {
		this.codecRegistrar = codecRegistrar;
	}

	@SuppressWarnings("unchecked")
	public JsonElement serialize(SerializableObject objectToSerialize) {
		Class<?> clazz = objectToSerialize.getClass();
		GenericCodec<SerializableObject> codec = (GenericCodec<SerializableObject>) codecRegistrar.getCodec(clazz);
		return codec.serialize(objectToSerialize, null, null);
	}

	@SuppressWarnings("unchecked")
	public SerializableObject deserialize(JsonElement objectToDeserialize, Class<SerializableObject> clazz) {
		GenericCodec<SerializableObject> codec = (GenericCodec<SerializableObject>) codecRegistrar.getCodec(clazz);
		return codec.deserialize(objectToDeserialize, null, null);
	}

}
