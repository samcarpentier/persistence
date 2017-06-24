package database.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import database.service.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseClientFactoryTest {

  private static final String DATABASE_NAME = "databaseName";
  private DatabaseClientFactory databaseClientFactory;

  @Before
  public void setUp() {
    this.databaseClientFactory = new DatabaseClientFactory();
  }

  @Test
  public void givenFileIODatabaseType_whenCreateClient_thenReturnFileIODatabase() {
    DatabaseClient databaseClient = databaseClientFactory.createClient(DatabaseType.FILE_IO_DB, DATABASE_NAME);
    assertThat(databaseClient, instanceOf(FileIODatabaseClient.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenUnknownType_whenCreateClient_thenThrowIllegalArgumentException() {
    databaseClientFactory.createClient(null, DATABASE_NAME);
    fail("Expected IllegalArgumentException but was never thrown.");
  }
}
