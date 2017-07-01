package database.service.fileio.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import database.service.exception.interaction.*;
import database.service.fileio.model.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {

  private static final String COLLECTION_NAME = "collectionName";

  private Database database;

  @Before
  public void setUp() {
    this.database = new Database();
  }

  @Test
  public void givenCollectionName_whenAddCollection_thenCollectionIsAdded() throws Exception {
    database.addCollection(COLLECTION_NAME);
    assertThat(database.getCollection(COLLECTION_NAME), is(not(nullValue())));
  }

  @Test(expected = DuplicateCollectionException.class)
  public void givenExistingCollectionName_whenAddCollection_thenThrowDuplicateCollectionException() throws Exception {
    database.addCollection(COLLECTION_NAME);
    database.addCollection(COLLECTION_NAME);
    fail("Expected DuplicateCollectionException but was never thrown.");
  }

  @Test
  public void givenExistingCollection_whenGetCollection_thenReturnCollection() throws Exception {
    database.addCollection(COLLECTION_NAME);
    DatabaseCollection collection = database.getCollection(COLLECTION_NAME);
    assertThat(collection, is(not(nullValue())));
  }

  @Test(expected = CollectionNotFoundException.class)
  public void givenNoCollectionAssociatedWithGivenName_whenGetCollection_thenThrowCollectionNotFoundException()
      throws Exception {
    database.getCollection(COLLECTION_NAME);
    fail("Expected CollectionNotFoundException but was never thrown.");
  }
}
