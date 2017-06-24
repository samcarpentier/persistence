package serialization.manager.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.*;

import serialization.manager.service.annotation.Id;
import serialization.manager.service.codec.TestClass;
import serialization.manager.service.exception.SerializationException;

public class ObjectFormatProcessorTest {

  private ObjectFormatProcessor objectFormatProcessor;

  @Before
  public void setUp() {
    this.objectFormatProcessor = new ObjectFormatProcessor();
  }

  @Test
  public void givenClassWithIdAnnotation_whenGetIdField_thenReturnFieldWithIdAnnotation() throws Exception {
    Field idField = objectFormatProcessor.getIdField(TestClass.class);
    assertThat(idField.getName(), is("arg1"));
  }

  @Test(expected = SerializationException.class)
  public void givenClassWithTwoIdAnnotations_whenGetIdField_thenSerializationException() throws Exception {
    objectFormatProcessor.getIdField(ClassWithTwoIds.class);
    fail("Expected SerializationException but was never thrown.");
  }

  @Test(expected = SerializationException.class)
  public void givenClassWithoutIdAnnotation_whenGetIdField_thenThrowSerializationException() throws Exception {
    objectFormatProcessor.getIdField(ClassWithoutId.class);
    fail("Expected SerializationException but was never thrown.");
  }

  private class ClassWithTwoIds implements SerializableObject {
    @Id
    String arg1;
    @Id
    String arg2;
  }

  // Protected to remove unused parameter error.
  protected class ClassWithoutId implements SerializableObject {
    String arg1;
  }

}
