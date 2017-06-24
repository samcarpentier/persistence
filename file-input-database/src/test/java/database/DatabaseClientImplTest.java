package database;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.JsonObject;

import database.model.*;
import serialization.*;
import serialization.annotation.Id;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseClientImplTest {

  private static final String DATABASE_NAME = "databaseName";
  private static final String COLLECTION_NAME = "collectionName";

  private static final String ID = "someId";

  private DatabaseClientImpl databaseClient;

  @Mock
  private DatabaseFileIOManager ioManager;
  @Mock
  private SerializationManager serializationManager;

  @Mock
  private Database database;
  @Mock
  private SerializableObject serializableObject;
  @Mock
  private DatabaseCollection collection;

  private JsonObject serializedEntry = new JsonObject();;

  @Before
  public void setUp() throws Exception {
    given(database.getName()).willReturn(DATABASE_NAME);
    given(ioManager.loadFromFile(DATABASE_NAME)).willReturn(database);

    this.databaseClient = new DatabaseClientImpl(ioManager, serializationManager);
  }

  @Test
  public void givenDatabaseName_whenOpenDatabase_thenLoadDatabaseFromIOManager() throws Exception {
    databaseClient.openDatabase(DATABASE_NAME);
    verify(ioManager).loadFromFile(DATABASE_NAME);
  }

  @Test
  public void givenLoadedDatabase_whenCloseDatabase_thenWriteDatabaseUsingIOManager() throws Exception {
    databaseClient.openDatabase(DATABASE_NAME);
    databaseClient.closeDatabase();
    verify(ioManager).writeToFile(database, DATABASE_NAME);
  }

  @Test
  public void givenDatabaseNotLoaded_whenCloseDatabase_thenDoNotUseIOManager() throws Exception {
    databaseClient.closeDatabase();
    verify(ioManager, never()).writeToFile(database, DATABASE_NAME);
  }

  @Test
  public void givenCollectionName_whenCreateCollection_thenCollectionIsCreatedInDatabase() throws Exception {
    databaseClient.openDatabase(DATABASE_NAME);

    databaseClient.createCollection(COLLECTION_NAME);

    verify(database).addCollection(COLLECTION_NAME);
  }

  @Test
  public void givenSerializableObject_whenSave_thenVerifySerializedEntryIsAddedToCollection() throws Exception {
    given(serializationManager.serialize(serializableObject)).willReturn(serializedEntry);
    given(database.getCollection(COLLECTION_NAME)).willReturn(collection);
    databaseClient.openDatabase(DATABASE_NAME);

    databaseClient.save(serializableObject, COLLECTION_NAME);

    verify(collection).addEntry(serializedEntry);
  }

  @Test
  public void givenIdAndCollectionName_whenFindById_thenReturnSerializableObjectAssociatedWithId() throws Exception {
    Dummy dummy = new Dummy();
    given(database.getCollection(COLLECTION_NAME)).willReturn(collection);
    given(serializationManager.deserialize(serializedEntry, Dummy.class)).willReturn(dummy);
    given(collection.findEntryById(ID)).willReturn(serializedEntry);
    databaseClient.openDatabase(DATABASE_NAME);

    Dummy retrievedEntry = (Dummy) databaseClient.findById(COLLECTION_NAME, ID, Dummy.class);

    assertThat(retrievedEntry, is(dummy));
  }

  protected class Dummy implements SerializableObject {

    @Id
    String arg1;
    String arg2;

    public Dummy() {
    }
  }

}
