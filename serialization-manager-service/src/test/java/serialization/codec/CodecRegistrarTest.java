package serialization.codec;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.runners.*;

import com.google.gson.*;

import serialization.*;

@RunWith(MockitoJUnitRunner.class)
public class CodecRegistrarTest {

	private class Dummy implements SerializableObject {
	}

	private class DummyCodec extends GenericCodec<Dummy> {

		@Override
		public JsonElement serialize(Dummy object, Type type, JsonSerializationContext context) {
			return null;
		}

		@Override
		public Dummy deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
			return null;
		}

	}

	private CodecRegistrar codecRegistrar;

	@Before
	public void setUp() {
		this.codecRegistrar = new CodecRegistrar();
	}

	@Test
	public void givenClassAndRelatedCodec_whenAddCodec_thenCodecIsAddedInMap() {
		// given
		DummyCodec codec = new DummyCodec();

		// when
		codecRegistrar.registerCodec(Dummy.class, codec);

		// then
		GenericCodec<?> retrievedCodec = codecRegistrar.getCodec(Dummy.class);
		assertThat(retrievedCodec, instanceOf(DummyCodec.class));
	}

}
