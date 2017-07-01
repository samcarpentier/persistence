package database.service.fileio.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;

import com.google.gson.JsonObject;

import database.service.exception.interaction.*;
import database.service.fileio.model.DatabaseCollection;
import util.commons.PersistenceConfig;

public class DatabaseCollectionTest {

  private static final String COLLECTION_NAME = "collectionName";

  private static final String ID_FIELD = "idField";
  private static final String ID = "someId";
  private static final String OTHER_ID = "otherId";

  private DatabaseCollection collection;

  @Before
  public void setUp() {
    this.collection = new DatabaseCollection(COLLECTION_NAME);
  }

  @Test
  public void givenJsonObject_whenAddEntry_thenEntryIsAddedInCollection() throws Exception {
    JsonObject entry = givenDefaultEntry();
    collection.addEntry(entry);
    assertThat(collection.getEntries(), contains(entry));
  }

  @Test(expected = DuplicateIdException.class)
  public void givenEntryWithSameIdThanAlreadyExistingEntry_whenAddEntry_thenThrowNewDuplicateIdException()
      throws Exception {
    // given
    JsonObject entry = new JsonObject();
    entry.addProperty(PersistenceConfig.ID_FIELD_IDENTIFIER, ID_FIELD);
    entry.addProperty(ID_FIELD, ID);
    collection.addEntry(entry);

    JsonObject otherEntryWithSameId = new JsonObject();
    otherEntryWithSameId.addProperty(PersistenceConfig.ID_FIELD_IDENTIFIER, ID_FIELD);
    otherEntryWithSameId.addProperty(ID_FIELD, ID);

    // when
    collection.addEntry(otherEntryWithSameId);

    // then
    fail("Expected DuplicateIdException but was never thrown.");
  }

  @Test
  public void givenMultipleEntriesInCollection_whenRemoveEntryForId_thenOnlySpecifiedEntryIsRemoved() throws Exception {
    JsonObject entry = givenDefaultEntry();
    JsonObject otherEntry = new JsonObject();
    otherEntry.addProperty(PersistenceConfig.ID_FIELD_IDENTIFIER, ID_FIELD);
    otherEntry.addProperty(ID_FIELD, OTHER_ID);

    collection.addEntry(entry);
    collection.addEntry(otherEntry);

    collection.removeEntryForId(ID);

    assertThat(collection.getEntries(), contains(otherEntry));
    assertThat(collection.getEntries(), not(hasItem(entry)));
  }

  @Test
  public void givenEntryNotInCollection_whenRemoveEntryForId_thenNoEntryIsRemoved() throws Exception {
    JsonObject entry = givenDefaultEntry();
    collection.addEntry(entry);

    collection.removeEntryForId(OTHER_ID);

    assertThat(collection.getEntries(), contains(entry));
  }

  @Test
  public void givenId_whenFindById_thenReturnEntryAssociatedWithId() throws Exception {
    JsonObject entry = givenDefaultEntry();
    collection.addEntry(entry);

    JsonObject retrievedEntry = collection.findEntryById(ID);

    assertThat(retrievedEntry, is(entry));
  }

  @Test(expected = EntryNotFoundException.class)
  public void givenIdWithoutAssociatedEntry_whenFindById_thenThrowEntryNotFoundException() throws Exception {
    collection.findEntryById(ID);
    fail("Expected EntryNotFoundException but was never thrown.");
  }

  private JsonObject givenDefaultEntry() {
    JsonObject entry = new JsonObject();

    entry.addProperty(PersistenceConfig.ID_FIELD_IDENTIFIER, ID_FIELD);
    entry.addProperty(ID_FIELD, ID);

    return entry;
  }
}
