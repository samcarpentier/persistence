package database.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.nio.file.*;
import java.util.stream.Collectors;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.*;

import database.service.model.Database;
import serialization.manager.service.SerializationManager;
import util.commons.*;

@RunWith(MockitoJUnitRunner.class)
public class FileIOManagerTest {

  private static final String DATABASE_NAME = "databaseName";
  private static final String DATABASE_COLLECTIONS = "{\"collectionName\": {\"name\":\"collectionName\",\"entries\":[]}}";

  private FileIOManager ioManager;

  @Mock
  private SerializationManager serializationManager;

  @Mock
  private Database database;

  @Before
  public void setUp() throws Exception {
    given(database.getName()).willReturn(DATABASE_NAME);
    this.ioManager = new FileIOManager(serializationManager);
  }

  @Test
  public void givenDatabase_whenWriteToFile_thenWriteSerializedDatabaseToFile() throws Exception {
    // given
    JsonObject serializedDatabase = givenSerializedDatabase();
    given(serializationManager.serialize(database)).willReturn(serializedDatabase);

    // when
    ioManager.writeToFile(database);

    // then
    String retrievedDatabase = readDatabaseWrittenInFile();
    assertThat(retrievedDatabase.toString(), equalTo(retrievedDatabase));
  }

  @Test
  public void givenDatabaseInFile_whenLoadFromFile_thenLoadSerializedDatabaseIntoObjectCorrectly() throws Exception {
    // given
    JsonObject serializedDatabase = givenSerializedDatabase();
    given(serializationManager.serialize(database)).willReturn(serializedDatabase);
    ioManager.writeToFile(database);

    String databaseInFile = readDatabaseWrittenInFile();
    given(serializationManager.deserialize(JsonObjectProvider.fromString(databaseInFile), Database.class))
        .willReturn(database);

    // when
    Database retrievedDatabase = ioManager.loadFromFile(DATABASE_NAME);

    // then
    assertThat(retrievedDatabase.getName(), is(database.getName()));
  }

  private JsonObject givenSerializedDatabase() {
    JsonObject serializedDatabase = new JsonObject();

    serializedDatabase.addProperty("id", "name");
    serializedDatabase.addProperty("name", DATABASE_NAME);
    serializedDatabase.add("collections", new Gson().fromJson(DATABASE_COLLECTIONS, JsonObject.class));

    return serializedDatabase;
  }

  private String readDatabaseWrittenInFile() throws Exception {
    return Files
        .lines(Paths.get(PersistenceConfig.DEFAULT_DATABASE_LOCATION,
            DATABASE_NAME + PersistenceConfig.SERIALIZED_FILE_EXTENSION))
        .collect(Collectors.toList())
        .get(0);
  }

}
