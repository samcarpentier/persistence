package database.model;

import java.io.Serializable;
import java.util.*;

import com.google.common.collect.Maps;

public class Database implements Serializable {

  private static final long serialVersionUID = 5712719555769453715L;

  private String name;
  private Map<String, DatabaseCollection> collections = Maps.newHashMap();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, DatabaseCollection> getAllCollections() {
    return collections;
  }

  public Optional<DatabaseCollection> getCollection(String collectionName) {
    return Optional.ofNullable(collections.get(collectionName));
  }

  public void setCollections(Map<String, DatabaseCollection> collections) {
    this.collections = collections;
  }

  public void addCollection(String collectionName) {
    this.collections.put(collectionName, new DatabaseCollection());
  }

}
