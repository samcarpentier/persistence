package database.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;

import com.google.gson.JsonObject;

import database.exception.interaction.EntryNotFoundException;
import serialization.SerializationManagerConstants;

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
  public void givenEntryInCollection_whenRemoveEntryForId_thenEntryIsRemoved() {
    JsonObject entry = new JsonObject();
    entry.addProperty(SerializationManagerConstants.ID_FIELD_IDENTIFIER, ID);
    collection.addEntry(entry);

    collection.removeEntryForId(ID);

    assertThat(collection.getEntries(), is(empty()));
  }

  @Test
  public void givenEntryNotInCollection_whenRemoveEntryForId_thenNoEntryIsRemoved() {
    JsonObject entry = new JsonObject();
    entry.addProperty(SerializationManagerConstants.ID_FIELD_IDENTIFIER, ID);
    collection.addEntry(entry);

    collection.removeEntryForId(OTHER_ID);

    assertThat(collection.getEntries(), contains(entry));
  }

  @Test
  public void givenId_whenFindById_thenReturnEntryAssociatedWithId() throws Exception {
    JsonObject entry = new JsonObject();
    entry.addProperty(SerializationManagerConstants.ID_FIELD_IDENTIFIER, ID);
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
