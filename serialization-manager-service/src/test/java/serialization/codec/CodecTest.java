package serialization.codec;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.JsonObject;

import serialization.util.ObjectFormatProcessor;

@RunWith(MockitoJUnitRunner.class)
public class CodecTest {

  private static final String VALUE_1 = "value1";
  private static final String VALUE_2 = "value2";

  private Codec codec;
  private ObjectFormatProcessor objectFormatProcessor;
  private TestClass testClass;

  @Before
  public void setUp() {
    testClass = new TestClass();
    testClass.setArg1(VALUE_1);
    testClass.setArg2(VALUE_2);

    objectFormatProcessor = new ObjectFormatProcessor();
    codec = new Codec(objectFormatProcessor);
  }

  @Test
  public void givenObject_whenToJson_thenReturnJsonObjectCorrectlyFormatted() throws Exception {
    JsonObject serializedObject = codec.toJson(testClass);

    assertThat(serializedObject.get("id").getAsString(), is("arg1"));
    assertThat(serializedObject.get("arg1").getAsString(), is(VALUE_1));
    assertThat(serializedObject.get("arg2").getAsString(), is(VALUE_2));
  }

  @Test
  public void givenJson_whenFromJson_thenReturnObjectCorrectlyFormatted() throws Exception {
    JsonObject serializedObject = codec.toJson(testClass);

    TestClass deserializedObject = (TestClass) codec.fromJson(serializedObject, TestClass.class);

    assertThat(deserializedObject.getArg1(), is(VALUE_1));
    assertThat(deserializedObject.getArg2(), is(VALUE_2));
  }

}
