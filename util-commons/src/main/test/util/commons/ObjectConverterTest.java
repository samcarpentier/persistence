package util.commons;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.*;

import com.google.common.collect.Sets;

public class ObjectConverterTest {

  private static final String VALUE = "value";
  private static final String OTHER_VALUE = "otherValue";

  private ObjectConverter<String, Object> converter;

  @Before
  public void setUp() {
    this.converter = new ObjectConverter<>();
  }

  @Test
  public void givenStringAsObject_whenConvert_thenOriginalStringIsRetrieved() {
    String converted = converter.convert((Object) VALUE);
    assertThat(converted, is(VALUE));
  }

  @Test
  public void givenSetOfStringsAsSetOfObjects_whenConvertAll_thenSetOfOriginalStringsAreRetrieved() {
    Set<Object> objects = Sets.newHashSet(VALUE, OTHER_VALUE);
    Set<String> converted = converter.convertAll(objects);
    assertThat(converted, containsInAnyOrder(VALUE, OTHER_VALUE));
  }
}
