package database;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import database.exception.interaction.DuplicateCollectionException;
import database.model.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseClientImplTest {

  private static final String DATABASE_NAME = "databaseName";
  private static final String COLLECTION_NAME = "collectionName";

  private DatabaseClientImpl databaseClient;

  @Mock
  private DatabaseFileIOManager ioManager;

  @Mock
  private Database database;
  private Optional<DatabaseCollection> collection;

  @Before
  public void setUp() throws Exception {
    given(database.getName()).willReturn(DATABASE_NAME);
    given(ioManager.loadFromFile(DATABASE_NAME)).willReturn(database);

    collection = Optional.ofNullable(new DatabaseCollection());
    this.databaseClient = new DatabaseClientImpl(ioManager);
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
    given(database.getCollection(COLLECTION_NAME)).willReturn(Optional.ofNullable(null));
    databaseClient.openDatabase(DATABASE_NAME);

    databaseClient.createCollection(COLLECTION_NAME);

    verify(database).addCollection(COLLECTION_NAME);
  }

  @Test(expected = DuplicateCollectionException.class)
  public void givenAlreadyExistingCollection_whenCreateCollection_thenThrowDatabaseInteractionException()
      throws Exception {
    given(database.getCollection(COLLECTION_NAME)).willReturn(collection);
    databaseClient.openDatabase(DATABASE_NAME);

    databaseClient.createCollection(COLLECTION_NAME);

    fail("Expected DuplicateCollectionException but was never thrown.");
  }

}
