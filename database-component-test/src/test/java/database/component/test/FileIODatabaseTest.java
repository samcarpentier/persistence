package database.component.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.nio.file.*;
import java.util.Set;

import org.junit.*;

import com.google.common.collect.Lists;

import database.service.*;
import database.service.exception.interaction.*;
import util.commons.*;

public class FileIODatabaseTest {

  private static final String DATABASE_NAME = "motorcycle-database";
  private static final String COLLECTION_NAME = MotorCycle.class.getName();

  private static final String VIN = "9876543210";
  private static final String OTHER_VIN = "0123456789";
  private static final String MAKE = "Suzuki";
  private static final String MODEL = "Hayabusa";
  private static final int DISPLACEMENT = 1340;

  private static DatabaseClientFactory databaseClientFactory;
  private static DatabaseClient databaseClient;

  @BeforeClass
  public static void beforeClass() throws Exception {
    deleteDatabaseFile();

    databaseClientFactory = new DatabaseClientFactory();
    databaseClient = databaseClientFactory.createClient(DatabaseType.FILE_IO_DB, DATABASE_NAME);
  }

  private static void deleteDatabaseFile() throws Exception {
    Files.deleteIfExists(Paths.get(PersistenceConfig.DEFAULT_DATABASE_LOCATION,
        DATABASE_NAME + PersistenceConfig.SERIALIZED_FILE_EXTENSION));
  }

  @Before
  public void setUp() throws Exception {
    databaseClient.clearCollections();
    databaseClient.createCollection(COLLECTION_NAME);
  }

  @Test
  public void givenNoObjectInDatabase_whenSave_thenObjectIsInDatabase() throws Exception {
    // given
    MotorCycle motorCycle = givenMotorCycle(VIN);

    // when
    databaseClient.save(motorCycle, COLLECTION_NAME);

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
    MotorCycle motorCycle = givenMotorCycle(VIN);
    MotorCycle motorCycleWithSameId = givenMotorCycle(VIN);
    databaseClient.save(motorCycle, COLLECTION_NAME);

    // when
    databaseClient.save(motorCycleWithSameId, COLLECTION_NAME);

    // then
    Assertions.expect(DuplicateIdException.class);
  }

  @Test
  public void givenMultipleObjectsInDatabase_whenFindById_thenReturnObjectAssociatedWithId() throws Exception {
    // given
    MotorCycle motorCycle = givenMotorCycle(VIN);
    MotorCycle otherMotorCycle = givenMotorCycle(OTHER_VIN);
    databaseClient.save(motorCycle, COLLECTION_NAME);
    databaseClient.save(otherMotorCycle, COLLECTION_NAME);

    // when
    MotorCycle retrievedMotorCycle = (MotorCycle) databaseClient.findById(COLLECTION_NAME, MotorCycle.class, VIN);

    // then
    new MotorCycleVerifier(retrievedMotorCycle).assertThatVin(is(VIN));
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Test
  public void givenMultipleObjectsInDatabase_whenFindByIds_thenReturnAllObjectsAssociatedWithIds() throws Exception {
    // given
    MotorCycle motorCycle = givenMotorCycle(VIN);
    MotorCycle otherMotorCycle = givenMotorCycle(OTHER_VIN);
    databaseClient.save(motorCycle, COLLECTION_NAME);
    databaseClient.save(otherMotorCycle, COLLECTION_NAME);

    // when
    Set<MotorCycle> retrievedMotorCycles = (Set) databaseClient.findByIds(COLLECTION_NAME,
        MotorCycle.class,
        VIN,
        OTHER_VIN);

    // then
    retrievedMotorCycles.forEach(retrievedMotorCycle -> {
      assertThat(retrievedMotorCycle.getVin(), isIn(Lists.newArrayList(VIN, OTHER_VIN)));

      new MotorCycleVerifier(retrievedMotorCycle)
          .assertThatMake(is(MAKE))
          .assertThatModel(is(MODEL))
          .assertThatDisplacement(is(DISPLACEMENT));
    });
  }

  @Test(expected = EntryNotFoundException.class)
  public void givenMultipleObjectsInDatabase_whenRemove_thenObjectAssociatedWithIdIsRemovedAndOthersAreUntouched()
      throws Exception {
    // given
    MotorCycle motorCycle = givenMotorCycle(VIN);
    MotorCycle otherMotorCycle = givenMotorCycle(OTHER_VIN);
    databaseClient.save(motorCycle, COLLECTION_NAME);
    databaseClient.save(otherMotorCycle, COLLECTION_NAME);

    // when
    databaseClient.remove(COLLECTION_NAME, VIN);

    // then
    MotorCycle retrievedOtherMotorCycle = (MotorCycle) databaseClient.findById(COLLECTION_NAME,
        MotorCycle.class,
        OTHER_VIN);
    assertThat(retrievedOtherMotorCycle, is(not(nullValue())));

    databaseClient.findById(COLLECTION_NAME, MotorCycle.class, VIN);
    Assertions.expect(EntryNotFoundException.class);
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
    MotorCycle motorCycle = givenMotorCycle(VIN);
    databaseClient.save(motorCycle, COLLECTION_NAME);

    // when
    databaseClient.closeDatabase();

    // then
    DatabaseClient otherClient = databaseClientFactory.createClient(DatabaseType.FILE_IO_DB, DATABASE_NAME);
    otherClient.findById(COLLECTION_NAME, MotorCycle.class, VIN);
  }

  private MotorCycle givenMotorCycle(String vin) {
    MotorCycle motorCycle = new MotorCycle();

    motorCycle.setVin(vin);
    motorCycle.setMake(MAKE);
    motorCycle.setModel(MODEL);
    motorCycle.setDisplacement(DISPLACEMENT);

    return motorCycle;
  }
}
