package serialization;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;

import com.google.gson.JsonObject;

import serialization.exception.*;

public class SerializationManagerTest {

  private static final String VIN = "0123456789";
  private static final String MAKE = "Volkswagen";
  private static final String MODEL = "Golf R";
  private static final int YEAR = 2018;
  private static final String OPTION_1 = "conditionned air";
  private static final String OPTION_2 = "heating seats";

  private static SerializationManager serializationManager;

  @BeforeClass
  public static void beforeClass() {
    serializationManager = new SerializationManagerFactory().create();
  }

  @Test
  public void givenObjectWithId_whenSerialize_thenReturnJsonObjectCorrectlyFormatted() throws Exception {
    // given
    Car car = givenDefaultCar();

    // when
    JsonObject serializedObject = serializationManager.serialize(car);

    // then
    new CarVerifier(serializedObject.toString())
        .assertThatVin(is(VIN))
        .assertThatMake(is(MAKE))
        .assertThatModel(is(MODEL))
        .assertThatYear(is(YEAR))
        .assertThatOptions(containsInAnyOrder(OPTION_1, OPTION_2));
  }

  @Test(expected = SerializationException.class)
  public void givenObjectWithTwoIds_whenSerialize_thenThrowSerializationException() throws Exception {
    // given
    ObjectWithTwoIds invalid = new ObjectWithTwoIds();

    // when
    serializationManager.serialize(invalid);

    // then exception is thrown
    fail("Expected SerializationException but was never thrown.");
  }

  @Test(expected = SerializationException.class)
  public void givenObjectWithoutIdField_whenSerialize_thenThrowSerializationException() throws Exception {
    // given
    ObjectWithoutId invalid = new ObjectWithoutId();

    // when
    serializationManager.serialize(invalid);

    // then exception is thrown
    fail("Expected SerializationException but was never thrown.");
  }

  @Test(expected = SerializationException.class)
  public void givenInvalidObject_whenSerialize_thenThrowSerializationException() throws Exception {
    // when
    serializationManager.serialize(null);

    // then exception is thrown
    fail("Expected SerializationException but was never thrown.");
  }

  @Test
  public void givenSerializedObject_whenDeserialize_thenReturnOriginalObjectCorrectlyFormatted() throws Exception {
    // given
    Car car = givenDefaultCar();
    JsonObject serializedObject = serializationManager.serialize(car);

    // when
    Car deserializedObject = (Car) serializationManager.deserialize(serializedObject, Car.class);

    // then
    assertThat(deserializedObject.getVin(), is(VIN));
    assertThat(deserializedObject.getMake(), is(MAKE));
    assertThat(deserializedObject.getModel(), is(MODEL));
    assertThat(deserializedObject.getYear(), is(YEAR));
    assertThat(deserializedObject.getOptions(), containsInAnyOrder(OPTION_1, OPTION_2));
  }

  @Test(expected = DeserializationException.class)
  public void givenInvalidSerializedObject_whenDeserialize_thenThrowDeserializationException() throws Exception {
    // when
    serializationManager.deserialize(null, Car.class);

    // then exception is thrown
    fail("Expected DeserializationException but was never thrown.");
  }

  private Car givenDefaultCar() {
    Car car = new Car();

    car.setVin(VIN);
    car.setMake(MAKE);
    car.setModel(MODEL);
    car.setYear(YEAR);
    car.addOption(OPTION_1);
    car.addOption(OPTION_2);

    return car;
  }

}
