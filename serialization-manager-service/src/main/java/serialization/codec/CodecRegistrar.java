package serialization.codec;

import java.util.Map;

import com.google.common.collect.Maps;

import serialization.SerializableObject;

public class CodecRegistrar {

	private Map<Class<? extends SerializableObject>, GenericCodec<?>> registeredCodecs;

	public CodecRegistrar() {
		registeredCodecs = Maps.newHashMap();
	}

	public void registerCodec(Class<? extends SerializableObject> clazz, GenericCodec<?> codec) {
		registeredCodecs.put(clazz, codec);
	}

	public GenericCodec<?> getCodec(Class<?> clazz) {
		return registeredCodecs.get(clazz);
	}

}
