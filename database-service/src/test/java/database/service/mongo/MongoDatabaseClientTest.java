package database.service.mongo;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.bson.BsonDocument;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

import database.service.DatabaseStatus;
import database.service.exception.interaction.DuplicateCollectionException;
import util.commons.TestUtils;

@RunWith(MockitoJUnitRunner.class)
public class MongoDatabaseClientTest {

  private static final String DATABASE_NAME = "databaseName";
  private static final String OTHER_DATABASE_NAME = "otherDatabaseName";
  private static final String COLLECTION_NAME = "collectionName";

  private MongoDatabaseClient databaseClient;

  @Mock
  private MongoClient mongoClient;

  @Mock
  private MongoDatabase database;

  @Before
  public void setUp() throws Exception {
    given(mongoClient.getDatabase(DATABASE_NAME)).willReturn(database);
    this.databaseClient = new MongoDatabaseClient(DATABASE_NAME, mongoClient);
  }

  @Test
  public void givenDatabaseName_whenOpenDatabase_thenCreateDatabaseAndAssignItInClient() throws Exception {
    MongoDatabase otherDatabase = mock(MongoDatabase.class);
    given(mongoClient.getDatabase(OTHER_DATABASE_NAME)).willReturn(otherDatabase);

    databaseClient.openDatabase(OTHER_DATABASE_NAME, true);

    assertThat(databaseClient.getDatabase(), is(otherDatabase));
  }

  @Test
  public void givenDatabase_whenCloseDatabase_thenDatabaseIsClosed() throws Exception {
    databaseClient.closeDatabase();
    assertThat(databaseClient.getDatabase(), is(nullValue()));
  }

  @Test
  public void givenOpenedDatabase_whenGetDatabaseStatus_thenReturnOpenedStatus() throws Exception {
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
  public void givenNoCollection_whenCreateCollection_thenCollectionIsCreatedInDatabase() throws Exception {
    databaseClient.createCollection(COLLECTION_NAME);
    verify(database).createCollection(COLLECTION_NAME);
  }

  @Test(expected = DuplicateCollectionException.class)
  public void givenExistingCollectionWithSameName_whenCreateCollection_then() throws Exception {
    doThrow(new MongoCommandException(new BsonDocument(), new ServerAddress()))
        .when(database)
        .createCollection(COLLECTION_NAME);

    databaseClient.createCollection(COLLECTION_NAME);

    TestUtils.expect(DuplicateCollectionException.class);
  }

  @Test
  public void whenClearCollections_thenDropDatabase() {
    databaseClient.clearCollections();
    verify(database).drop();
  }

}
