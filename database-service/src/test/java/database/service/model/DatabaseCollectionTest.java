package database.service.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;

import com.google.gson.JsonObject;

import database.service.exception.interaction.EntryNotFoundException;
import database.service.model.DatabaseCollection;
import util.commons.PersistenceConfig;

public class DatabaseCollectionTest {

  private static final String COLLECTION_NAME = "collectionName";

  private static final String ID = "someId";
  private static final String OTHER_ID = "otherId";

  private DatabaseCollection collection;

  @Before
  public void setUp() {
    this.collection = new DatabaseCollection(COLLECTION_NAME);
  }

  @Test
  public void givenJsonObject_whenAddEntry_thenEntryIsAddedInCollection() {
    JsonObject entry = new JsonObject();
    collection.addEntry(entry);
    assertThat(collection.getEntries(), contains(entry));
  }

  @Test
  public void givenMultipleEntriesInCollection_whenRemoveEntryForId_thenOnlySpecifiedEntryIsRemoved() {
    JsonObject entry = new JsonObject();
    JsonObject otherEntry = new JsonObject();
    entry.addProperty(PersistenceConfig.ID_FIELD_IDENTIFIER, ID);
    otherEntry.addProperty(PersistenceConfig.ID_FIELD_IDENTIFIER, OTHER_ID);
    collection.addEntry(entry);
    collection.addEntry(otherEntry);

    collection.removeEntryForId(ID);

    assertThat(collection.getEntries(), contains(otherEntry));
    assertThat(collection.getEntries(), not(hasItem(entry)));
  }

  @Test
  public void givenEntryNotInCollection_whenRemoveEntryForId_thenNoEntryIsRemoved() {
    JsonObject entry = new JsonObject();
    entry.addProperty(PersistenceConfig.ID_FIELD_IDENTIFIER, ID);
    collection.addEntry(entry);

    collection.removeEntryForId(OTHER_ID);

    assertThat(collection.getEntries(), contains(entry));
  }

  @Test
  public void givenId_whenFindById_thenReturnEntryAssociatedWithId() throws Exception {
    JsonObject entry = new JsonObject();
    entry.addProperty(PersistenceConfig.ID_FIELD_IDENTIFIER, ID);
    collection.addEntry(entry);

    JsonObject retrievedEntry = collection.findEntryById(ID);

    assertThat(retrievedEntry, is(entry));
  }

  @Test(expected = EntryNotFoundException.class)
  public void givenIdWithoutAssociatedEntry_whenFindById_thenThrowEntryNotFoundException() throws Exception {
    collection.findEntryById(ID);
    fail("Expected EntryNotFoundException but was never thrown.");
  }
}
