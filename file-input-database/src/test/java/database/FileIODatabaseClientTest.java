package database;

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

import database.model.*;
import serialization.*;
import serialization.annotation.Id;
import util.commons.*;

@RunWith(MockitoJUnitRunner.class)
public class FileIODatabaseClientTest {

  private static final String DATABASE_NAME = "databaseName";
  private static final String COLLECTION_NAME = "collectionName";

  private static final String ID = "someId";
  private static final String OTHER_ID = "otherId";

  private FileIODatabaseClient databaseClient;

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

  private JsonObject serializedEntry = new JsonObject();
  private JsonObject otherSerializedEntry = new JsonObject();

  @Before
  public void setUp() throws Exception {
    serializedEntry.addProperty("name", "value1");
    otherSerializedEntry.addProperty("name", "value2");

    given(database.getName()).willReturn(DATABASE_NAME);
    given(ioManager.loadFromFile(DATABASE_NAME)).willReturn(database);
    given(database.getCollection(COLLECTION_NAME)).willReturn(collection);

    this.databaseClient = new FileIODatabaseClient(DATABASE_NAME, ioManager, serializationManager);
  }

  @Test
  public void givenLoadedDatabase_whenCloseDatabase_thenWriteDatabaseUsingIOManager() throws Exception {
    databaseClient.closeDatabase();
    verify(ioManager).writeToFile(database, DATABASE_NAME);
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

  protected class Dummy implements SerializableObject {
    @Id
    String arg1;
    String arg2;

    public Dummy() {
    }
  }

}
