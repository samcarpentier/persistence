package serialization;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.JsonElement;

import serialization.codec.*;

@RunWith(MockitoJUnitRunner.class)
public class SerializationManagerTest {

	private SerializationManager objectSerializer;

	@Mock
	private CodecRegistrar codecRegistrar;

	@Mock
	private GenericCodec<SerializableObject> codec;
	@Mock
	private JsonElement serializedObject;
	@Mock
	private SerializableObject object;

	@Before
	public void setUp() {
		doReturn(codec).when(codecRegistrar).getCodec(Mockito.any(Class.class));
		this.objectSerializer = new SerializationManager(codecRegistrar);
	}

	@Test
	public void givenObject_whenSerialize_thenFindCodecAndReturnSerializedObject() {
		given(codec.serialize(object, null, null)).willReturn(serializedObject);

		JsonElement json = objectSerializer.serialize(object);

		assertThat(json, is(serializedObject));
	}

	@Test
	public void givenJsonAndClass_whenDeserialize_thenFindCodecAndReturnDeserializedObject() {
		given(codec.deserialize(serializedObject, null, null)).willReturn(object);

		SerializableObject deserializedObject = objectSerializer.deserialize(serializedObject, SerializableObject.class);

		assertThat(deserializedObject, is(object));
	}

}
