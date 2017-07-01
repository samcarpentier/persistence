package database.service.fileio;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.JsonObject;

import database.service.*;
import database.service.exception.*;
import database.service.fileio.*;
import database.service.fileio.exception.DatabaseLoadingException;
import database.service.fileio.model.*;
import serialization.manager.service.*;
import serialization.manager.service.annotation.Id;
import util.commons.*;

@RunWith(MockitoJUnitRunner.class)
public class FileIODatabaseClientTest {

  private static final String DATABASE_NAME = "databaseName";
  private static final String OTHER_DATABASE_NAME = "otherDatabaseName";
  private static final String COLLECTION_NAME = "collectionName";

  private static final String ID = "someId";
  private static final String OTHER_ID = "otherId";

  private FileIODatabaseClient databaseClient;

  @Mock
  private FileIOManager ioManager;
  @Mock
  private SerializationManager serializationManager;

  @Mock
  private Database database;
  @Mock
  private SerializableObject serializableObject;
  @Mock
  private DatabaseCollection collection;

  private JsonObject serializedEntry = new JsonObject();
  private JsonObject otherSerializedEntry = new JsonObject();

  @AfterClass
  public static void afterClass() throws Exception {
    TestUtils.cleanupDatabaseFiles(DATABASE_NAME);
    TestUtils.cleanupDatabaseFiles(OTHER_DATABASE_NAME);
  }

  @Before
  public void setUp() throws Exception {
    serializedEntry.addProperty(PersistenceConfig.ID_FIELD_IDENTIFIER, "name");
    serializedEntry.addProperty("name", "value1");
    otherSerializedEntry.addProperty(PersistenceConfig.ID_FIELD_IDENTIFIER, "name");
    otherSerializedEntry.addProperty("name", "value2");

    given(database.getName()).willReturn(DATABASE_NAME);
    given(ioManager.loadFromFile(DATABASE_NAME)).willReturn(database);
    given(database.getCollection(COLLECTION_NAME)).willReturn(collection);

    this.databaseClient = new FileIODatabaseClient(DATABASE_NAME, ioManager, serializationManager);
  }

  @Test
  public void givenDoNotCreateIfAbsent_whenOpenDatabase_thenLoadDatabaseFromFile() throws Exception {
    given(ioManager.loadFromFile(OTHER_DATABASE_NAME)).willReturn(database);
    databaseClient.openDatabase(OTHER_DATABASE_NAME, false);
    verify(ioManager).loadFromFile(OTHER_DATABASE_NAME);
  }

  @Test
  public void givenCreateIfAbsent_whenOpenDatabase_thenLoadFromFile() throws Exception {
    given(ioManager.loadFromFile(OTHER_DATABASE_NAME)).willReturn(database);
    databaseClient.openDatabase(OTHER_DATABASE_NAME, true);
    verify(ioManager).loadFromFile(OTHER_DATABASE_NAME);
  }

  @Test
  public void givenCreateIfAbsentAndDatabaseLoadingException_whenOpenDatabase_createDatabase() throws Exception {
    given(ioManager.loadFromFile(OTHER_DATABASE_NAME)).willThrow(new DatabaseLoadingException(null));
    databaseClient.openDatabase(OTHER_DATABASE_NAME, true);
    // then no exception
  }

  @Test(expected = DatabaseLoadingException.class)
  public void givenDoNotCreateIfAbsentAndException_whenOpenDatabase_thenThrowException() throws Exception {
    given(ioManager.loadFromFile(OTHER_DATABASE_NAME)).willThrow(new DatabaseLoadingException(null));
    databaseClient.openDatabase(OTHER_DATABASE_NAME, false);
    TestUtils.expect(DatabaseLoadingException.class);
  }

  @Test
  public void givenLoadedDatabase_whenCloseDatabase_thenWriteDatabaseUsingIOManager() throws Exception {
    databaseClient.closeDatabase();
    verify(ioManager).writeToFile(database);
  }

  @Test
  public void givenOpenedDatabase_whenGetDatabaseStatus_thenReturnOpenedStatus() {
    DatabaseStatus status = databaseClient.getDatabaseStatus();
    assertThat(status, is(DatabaseStatus.OPENED));
  }

  @Test
  public void givenClosedDatabase_whenGetDatabaseStatus_thenReturnClosedStatus() throws Exception {
    databaseClient.closeDatabase();
    DatabaseStatus status = databaseClient.getDatabaseStatus();
    assertThat(status, is(DatabaseStatus.CLOSED));
  }

  @Test
  public void givenCollectionName_whenCreateCollection_thenCollectionIsCreatedInDatabase() throws Exception {
    databaseClient.createCollection(COLLECTION_NAME);
    verify(database).addCollection(COLLECTION_NAME);
  }

  @Test
  public void givenSerializableObject_whenSave_thenVerifySerializedEntryIsAddedToCollection() throws Exception {
    given(serializationManager.serialize(serializableObject)).willReturn(serializedEntry);
    databaseClient.save(serializableObject, COLLECTION_NAME);
    verify(collection).addEntry(serializedEntry);
  }

  @Test
  public void givenIdAndCollectionName_whenFindById_thenReturnSerializableObjectAssociatedWithId() throws Exception {
    Dummy dummy = new Dummy();
    given(collection.findEntryById(ID)).willReturn(serializedEntry);
    given(serializationManager.deserialize(serializedEntry, Dummy.class)).willReturn(dummy);

    Dummy retrievedEntry = (Dummy) databaseClient.findById(COLLECTION_NAME, Dummy.class, ID);

    assertThat(retrievedEntry, is(dummy));
  }

  @Test
  public void givenMultipleIdsAndCollectionName_whenFindByIds_thenReturnSerializableObjectsAssociatedWithIds()
      throws Exception {
    Dummy dummy1 = new Dummy();
    Dummy dummy2 = new Dummy();
    given(collection.findEntryById(ID)).willReturn(serializedEntry);
    given(collection.findEntryById(OTHER_ID)).willReturn(otherSerializedEntry);
    given(serializationManager.deserialize(serializedEntry, Dummy.class)).willReturn(dummy1);
    given(serializationManager.deserialize(otherSerializedEntry, Dummy.class)).willReturn(dummy2);

    Set<SerializableObject> retrievedEntries = databaseClient.findByIds(COLLECTION_NAME, Dummy.class, ID, OTHER_ID);

    assertThat(retrievedEntries, containsInAnyOrder(dummy1, dummy2));
  }

  @Test
  public void givenId_whenRemove_thenRemoveFromSpecifiedCollection() throws Exception {
    given(collection.findEntryById(ID)).willReturn(serializedEntry);
    given(collection.findEntryById(OTHER_ID)).willReturn(otherSerializedEntry);

    databaseClient.remove(COLLECTION_NAME, ID);

    verify(collection).removeEntryForId(ID);
  }

  @Test
  public void givenClosedDatabase_whenOpenDatabase_thenDatabaseIsReOpened() throws Exception {
    databaseClient.closeDatabase();
    databaseClient.openDatabase(DATABASE_NAME, true);
    // then no exception
  }

  @Test
  public void givenClosedDatabase_whenAnyOperationExceptOpenDatabase_thenThrowClosedDatabaseException()
      throws Exception {
    // given
    databaseClient.closeDatabase();

    // when, then
    assertThatClosedDatabaseExceptionIsThrownWhen(new CallBack() {
      @Override
      public void callback() throws Exception {
        databaseClient.closeDatabase();
      }
    });

    assertThatClosedDatabaseExceptionIsThrownWhen(new CallBack() {
      @Override
      public void callback() throws Exception {
        databaseClient.createCollection(COLLECTION_NAME);
      }
    });

    assertThatClosedDatabaseExceptionIsThrownWhen(new CallBack() {
      @Override
      public void callback() throws Exception {
        databaseClient.save(serializableObject, COLLECTION_NAME);
      }
    });

    assertThatClosedDatabaseExceptionIsThrownWhen(new CallBack() {
      @Override
      public void callback() throws Exception {
        databaseClient.findById(COLLECTION_NAME, SerializableObject.class, ID);
      }
    });

    assertThatClosedDatabaseExceptionIsThrownWhen(new CallBack() {
      @Override
      public void callback() throws Exception {
        databaseClient.findByIds(COLLECTION_NAME, SerializableObject.class, OTHER_ID);
      }
    });

    assertThatClosedDatabaseExceptionIsThrownWhen(new CallBack() {
      @Override
      public void callback() throws Exception {
        databaseClient.remove(COLLECTION_NAME, ID);
      }
    });

    assertThatClosedDatabaseExceptionIsThrownWhen(new CallBack() {
      @Override
      public void callback() throws Exception {
        databaseClient.clearCollections();
      }
    });
  }

  @Test
  public void whenClearCollections_thenClearCollectionsInDatabase() {
    databaseClient.clearCollections();
    verify(database).clearCollections();
  }

  private void assertThatClosedDatabaseExceptionIsThrownWhen(CallBack callback) throws Exception {
    try {
      callback.callback();
      TestUtils.expect(ClosedDatabaseException.class);
    } catch (ClosedDatabaseException e) {
      // Do nothing
    }
  }

  class Dummy implements SerializableObject {
    @Id
    String arg1;
    String arg2;

    public Dummy() {
    }
  }

  interface CallBack {
    void callback() throws Exception;
  }

}
