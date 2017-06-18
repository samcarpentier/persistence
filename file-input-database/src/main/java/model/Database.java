package model;

import java.util.Map;

import com.google.common.collect.Maps;

public class Database {

  private String name;
  private String path;
  private Map<String, DatabaseCollection> collections = Maps.newHashMap();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Map<String, DatabaseCollection> getAllCollections() {
    return collections;
  }

  public DatabaseCollection getCollection(String collectionName) {
    return collections.get(collectionName);
  }

  public void setCollections(Map<String, DatabaseCollection> collections) {
    this.collections = collections;
  }

}
