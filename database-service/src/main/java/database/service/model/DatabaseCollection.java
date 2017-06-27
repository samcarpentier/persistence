package database.service.model;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.*;

import database.service.exception.interaction.*;
import serialization.manager.service.SerializableObject;
import serialization.manager.service.annotation.Id;
import util.commons.PersistenceConfig;

public class DatabaseCollection implements SerializableObject {

  @Id
  private String name;
  private Set<JsonObject> entries = Sets.newHashSet();

  public DatabaseCollection() {
    // For deserialization
  }

  public DatabaseCollection(String name) {
    this.name = name;
    this.entries = Sets.newHashSet();
  }

  public void addEntry(JsonObject entry) throws DuplicateIdException {
    String idFieldValue = getIdFieldValue(entry);

    try {
      findEntryById(idFieldValue);
      throw new DuplicateIdException(
          String.format("Entry with ID [%s] already present in collection [%s]", idFieldValue, name));
    } catch (EntryNotFoundException e) {
      entries.add(entry);
    }
  }

  public void removeEntryForId(String id) {
    entries.removeIf(entry -> getIdFieldValue(entry) == id);
  }

  public JsonObject findEntryById(String id) throws EntryNotFoundException {
    System.out.println("AAA: " + entries);

    for (JsonObject entry : entries) {
      String idFieldValue = getIdFieldValue(entry);
      if (idFieldValue.equals(id)) {
        return entry;
      }
    }

    throw new EntryNotFoundException(String.format("No entry with ID [%s] found in collection [%s].", id, name));
  }

  public Set<JsonObject> getEntries() {
    return entries;
  }

  public void setEntries(Set<JsonObject> entries) {
    this.entries = entries;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  private String getIdFieldValue(JsonObject entry) {
    JsonElement e = entry.get(PersistenceConfig.ID_FIELD_IDENTIFIER);
    String idFIeld = e.getAsString();
    return entry.get(idFIeld).getAsString();
  }

}
