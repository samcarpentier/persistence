package database.model;

import java.io.Serializable;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

import database.exception.interaction.EntryNotFoundException;
import serialization.SerializationManagerConstants;

public class DatabaseCollection implements Serializable {

  private static final long serialVersionUID = -6466603262313845430L;

  private Set<JsonObject> entries = Sets.newHashSet();
  private String name;

  public DatabaseCollection() {
    // For deserialization
  }

  public DatabaseCollection(String name) {
    this.name = name;
    this.entries = Sets.newHashSet();
  }

  public void addEntry(JsonObject entry) {
    entries.add(entry);
  }

  public void removeEntryForId(String id) {
    entries.removeIf(entry -> entry.get(SerializationManagerConstants.ID_FIELD_IDENTIFIER).getAsString() == id);
  }

  public JsonObject findEntryById(String id) throws EntryNotFoundException {
    for (JsonObject entry : entries) {
      if (entry.get(SerializationManagerConstants.ID_FIELD_IDENTIFIER).getAsString() == id) {
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

}
