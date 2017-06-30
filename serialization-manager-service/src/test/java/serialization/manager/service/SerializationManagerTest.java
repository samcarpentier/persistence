package serialization.manager.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.JsonObject;

import serialization.manager.service.codec.*;
import serialization.manager.service.exception.DeserializationException;

@RunWith(MockitoJUnitRunner.class)
public class SerializationManagerTest {

  private SerializationManager serializationManager;
  private JsonObject json;

  @Mock
  private Codec codec;
  @Mock
  private TestClass object;

  @Before
  public void setUp() {
    this.serializationManager = new SerializationManager(codec);
  }

  @Test
  public void givenObject_whenSerialize_thenFindCodecAndReturnSerializedObject() throws Exception {
    given(codec.toJson(object)).willReturn(json);
    JsonObject serializedObject = serializationManager.serialize(object);
    assertThat(serializedObject, is(json));
  }

  @Test
  public void givenJsonAndClass_whenDeserialize_thenFindCodecAndReturnDeserializedObject()
      throws DeserializationException {
    given(codec.fromJson(json, TestClass.class)).willReturn(object);
    TestClass deserializedObject = (TestClass) serializationManager.deserialize(json, TestClass.class);
    assertThat(deserializedObject, is(object));
  }

}
