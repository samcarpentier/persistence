package database.component.tests;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.*;

import com.google.common.collect.Lists;

import database.service.*;
import database.service.exception.interaction.*;
import database.service.factory.*;
import serialization.manager.service.*;
import util.commons.*;

public class FileIODatabaseTest {

  private static final String DATABASE_NAME = "motorcycle-database";
  private static final String COLLECTION_NAME = MotorCycle.class.getName();

  private static final String VIN = "9876543210";
  private static final String OTHER_VIN = "0123456789";
  private static final String MAKE = "Suzuki";
  private static final String MODEL = "Hayabusa";
  private static final int DISPLACEMENT = 1340;

  private static DatabaseClientAbstractFactory<FileIODatabaseClient> databaseClientFactory;
  private static DatabaseClient databaseClient;
  private static ObjectConverter<MotorCycle, SerializableObject> serializableObjectConverter;

  @BeforeClass
  public static void beforeClass() throws Exception {
    TestUtils.cleanupDatabaseFiles(DATABASE_NAME);

    databaseClientFactory = new FileIODatabaseClientFactory(new SerializationManagerFactory().create());
    databaseClient = databaseClientFactory.create(DATABASE_NAME).getProxiedClient();
    serializableObjectConverter = new ObjectConverter<>();
  }

  @AfterClass
  public static void afterClass() throws Exception {
    TestUtils.cleanupDatabaseFiles(DATABASE_NAME);
  }

  @Before
  public void setUp() throws Exception {
    if (databaseClient.getDatabaseStatus() != DatabaseStatus.CLOSED) {
      databaseClient.clearCollections();
      databaseClient.createCollection(COLLECTION_NAME);
    } else {
      databaseClient.openDatabase(DATABASE_NAME, false);
      setUp();
    }
  }

  @Test
  public void givenNoObjectInDatabase_whenSave_thenObjectIsInDatabase() throws Exception {
    // given
    databaseClient.save(
        new MotorCycleBuilder().withVin(VIN).withMake(MAKE).withModel(MODEL).withDisplacement(DISPLACEMENT).build(),
        COLLECTION_NAME);

    // then
    MotorCycle retrievedMotorCycle = (MotorCycle) databaseClient.findById(COLLECTION_NAME, MotorCycle.class, VIN);
    new MotorCycleVerifier(retrievedMotorCycle)
        .assertThatVin(is(VIN))
        .assertThatMake(is(MAKE))
        .assertThatModel(is(MODEL))
        .assertThatDisplacement(is(DISPLACEMENT));
  }

  @Test(expected = DuplicateIdException.class)
  public void givenObjectInDatabase_whenSaveObjectWithSameId_thenThrowDuplicateIdException() throws Exception {
    // given
    databaseClient.save(new MotorCycleBuilder().withVin(VIN).build(), COLLECTION_NAME);

    // when
    databaseClient.save(new MotorCycleBuilder().withVin(VIN).build(), COLLECTION_NAME);

    // then
    TestUtils.expect(DuplicateIdException.class);
  }

  @Test
  public void givenMultipleObjectsInDatabase_whenFindById_thenReturnObjectAssociatedWithId() throws Exception {
    // given
    databaseClient.save(new MotorCycleBuilder().withVin(VIN).build(), COLLECTION_NAME);
    databaseClient.save(new MotorCycleBuilder().withVin(OTHER_VIN).build(), COLLECTION_NAME);

    // when
    MotorCycle retrievedMotorCycle = (MotorCycle) databaseClient.findById(COLLECTION_NAME, MotorCycle.class, VIN);

    // then
    new MotorCycleVerifier(retrievedMotorCycle).assertThatVin(is(VIN));
  }

  @Test
  public void givenMultipleObjectsInDatabase_whenFindByIds_thenReturnAllObjectsAssociatedWithIds() throws Exception {
    // given
    databaseClient.save(new MotorCycleBuilder().withVin(VIN).build(), COLLECTION_NAME);
    databaseClient.save(new MotorCycleBuilder().withVin(OTHER_VIN).build(), COLLECTION_NAME);

    // when
    Set<MotorCycle> retrievedMotorCycles = serializableObjectConverter
        .convertAll(databaseClient.findByIds(COLLECTION_NAME, MotorCycle.class, VIN, OTHER_VIN));

    // then
    retrievedMotorCycles.forEach(
        retrievedMotorCycle -> assertThat(retrievedMotorCycle.getVin(), isIn(Lists.newArrayList(VIN, OTHER_VIN))));
  }

  @Test
  public void givenMultipleObjectsInDatabase_whenRemove_thenObjectNotAssociatedWithIdAreUntouched() throws Exception {
    // given
    databaseClient.save(new MotorCycleBuilder().withVin(VIN).build(), COLLECTION_NAME);
    databaseClient.save(new MotorCycleBuilder().withVin(OTHER_VIN).build(), COLLECTION_NAME);

    // when
    databaseClient.remove(COLLECTION_NAME, VIN);

    // then
    MotorCycle retrievedOtherMotorCycle = (MotorCycle) databaseClient.findById(COLLECTION_NAME,
        MotorCycle.class,
        OTHER_VIN);
    assertThat(retrievedOtherMotorCycle, is(not(nullValue())));
  }

  @Test(expected = EntryNotFoundException.class)
  public void givenMultipleObjectsInDatabase_whenRemove_thenObjectAssociatedWithIdIsRemoved() throws Exception {
    // given
    databaseClient.save(new MotorCycleBuilder().withVin(VIN).build(), COLLECTION_NAME);
    databaseClient.save(new MotorCycleBuilder().withVin(OTHER_VIN).build(), COLLECTION_NAME);

    // when
    databaseClient.remove(COLLECTION_NAME, VIN);

    // then
    databaseClient.findById(COLLECTION_NAME, MotorCycle.class, VIN);
    TestUtils.expect(EntryNotFoundException.class);
  }

  @Test
  public void givenNoObjectInDatabase_whenRemove_thenDoNothing() throws Exception {
    // when
    databaseClient.remove(COLLECTION_NAME, VIN);

    // then no exception
  }

  @Test
  public void givenPopulatedDatabase_whenCloseDatabaseThenReopenDatabase_thenContentOfDatabaseIsPreserved()
      throws Exception {
    // given
    databaseClient.save(new MotorCycleBuilder().withVin(VIN).build(), COLLECTION_NAME);

    // when
    databaseClient.closeDatabase();

    // then
    DatabaseClient otherClient = databaseClientFactory.create(DATABASE_NAME).getProxiedClient();
    MotorCycle retrievedMotorCycle = (MotorCycle) otherClient.findById(COLLECTION_NAME, MotorCycle.class, VIN);
    new MotorCycleVerifier(retrievedMotorCycle).assertThatVin(is(VIN));
  }

}
