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

import com.google.common.collect.Sets;

import database.service.*;
import database.service.fileio.*;
import serialization.manager.service.SerializableObject;

@RunWith(MockitoJUnitRunner.class)
public class FileIODatabaseClientLoggingProxyTest {

  private static final String ID = "someId";
  private static final String OTHER_ID = "otherId";
  private static final String COLLECTION_NAME = "collectionName";

  private FileIODatabaseClientLoggingProxy loggingProxy;

  @Mock
  private FileIODatabaseClient databaseClient;

  @Mock
  private SerializableObject entry;

  @Before
  public void setUp() {
    this.loggingProxy = new FileIODatabaseClientLoggingProxy(databaseClient);
  }

  @Test
  public void givenDatabaseNameAndCreateIfAbsent_whenOpenDatabase_thenOpenDatabaseInClient() throws Exception {
    loggingProxy.openDatabase(COLLECTION_NAME, true);
    verify(databaseClient).openDatabase(COLLECTION_NAME, true);
  }

  @Test
  public void whenCloseDatabase_thenCloseDatabaseInClient() throws Exception {
    loggingProxy.closeDatabase();
    verify(databaseClient).closeDatabase();
  }

  @Test
  public void whenGetDatabaseStatus_thenGetDatabaseStatusInClient() {
    given(databaseClient.getDatabaseStatus()).willReturn(DatabaseStatus.OPENED);
    loggingProxy.getDatabaseStatus();
    verify(databaseClient).getDatabaseStatus();
  }

  @Test
  public void givenCollectionName_whenCreateCollection_thenCreateCollectionInClient() throws Exception {
    loggingProxy.createCollection(COLLECTION_NAME);
    verify(databaseClient).createCollection(COLLECTION_NAME);
  }

  @Test
  public void givenEntry_whenSave_thenSaveEntryInClient() throws Exception {
    loggingProxy.save(entry, COLLECTION_NAME);
    verify(databaseClient).save(entry, COLLECTION_NAME);
  }

  @Test
  public void givenId_whenFindById_thenFindByIdInClientAndReturnResult() throws Exception {
    given(databaseClient.findById(COLLECTION_NAME, SerializableObject.class, ID)).willReturn(entry);

    SerializableObject retrievedObject = loggingProxy.findById(COLLECTION_NAME, SerializableObject.class, ID);

    verify(databaseClient).findById(COLLECTION_NAME, SerializableObject.class, ID);
    assertThat(retrievedObject, is(entry));
  }

  @Test
  public void givenIds_whenFindByIds_thenFindByIdsInClientAndReturnResults() throws Exception {
    SerializableObject otherEntry = mock(SerializableObject.class);
    given(databaseClient.findByIds(COLLECTION_NAME, SerializableObject.class, ID, OTHER_ID))
        .willReturn(Sets.newHashSet(entry, otherEntry));

    Set<SerializableObject> retrivedObjects = loggingProxy.findByIds(COLLECTION_NAME,
        SerializableObject.class,
        ID,
        OTHER_ID);

    verify(databaseClient).findByIds(COLLECTION_NAME, SerializableObject.class, ID, OTHER_ID);
    assertThat(retrivedObjects, containsInAnyOrder(entry, otherEntry));
  }

  @Test
  public void givenId_whenRemove_thenRemoveInClient() throws Exception {
    loggingProxy.remove(COLLECTION_NAME, ID);
    verify(databaseClient).remove(COLLECTION_NAME, ID);
  }

  @Test
  public void whenClearCollections_thenClearCollectionsInClient() throws Exception {
    loggingProxy.clearCollections();
    verify(databaseClient).clearCollections();
  }
}
