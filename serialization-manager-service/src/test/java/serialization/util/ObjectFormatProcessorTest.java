package serialization.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.*;

import serialization.SerializableObject;
import serialization.annotation.Id;
import serialization.codec.TestClass;
import serialization.exception.SerializationException;

public class ObjectFormatProcessorTest {

  private ObjectFormatProcessor objectFormatProcessor;

  @Before
  public void setUp() {
    this.objectFormatProcessor = new ObjectFormatProcessor();
  }

  @Test
  public void givenClass_whenGetFields_thenReturnCollectionOfFields() {
    List<Field> fields = objectFormatProcessor.getFields(TestClass.class);
    List<String> fieldNames = fields.stream().map(field -> field.getName()).collect(Collectors.toList());
    assertThat(fieldNames, contains("arg1", "arg2", "val1"));
  }

  @Test
  public void givenClassWithIdAnnotation_whenGetIdField_thenReturnFieldWithIdAnnotation() throws Exception {
    Field idField = objectFormatProcessor.getIdField(TestClass.class);
    assertThat(idField.getName(), is("arg1"));
  }

  @Test(expected = SerializationException.class)
  public void givenClassWithTwoIdAnnotations_whenGetIdField_thenThrowIllegalStateException() throws Exception {
    objectFormatProcessor.getIdField(ClassWithTwoIds.class);
    fail("Expected SerializationException but was never thrown.");
  }

  private class ClassWithTwoIds implements SerializableObject {
    @Id
    String arg1;
    @Id
    String arg2;
  }

}
