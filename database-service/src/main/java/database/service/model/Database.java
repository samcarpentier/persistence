package database.service.model;

import java.util.*;

import com.google.common.collect.Maps;

import database.service.exception.interaction.*;
import serialization.manager.service.SerializableObject;
import serialization.manager.service.annotation.Id;

public class Database implements SerializableObject {

  @Id
  private String name;
  private Map<String, DatabaseCollection> collections = Maps.newHashMap();

  public Database() {
    // For deserialization
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, DatabaseCollection> getCollections() {
    return collections;
  }

  public DatabaseCollection getCollection(String collectionName) throws CollectionNotFoundException {
    return Optional.ofNullable(collections.get(collectionName)).orElseThrow(() -> new CollectionNotFoundException(""));
  }

  public void setCollections(Map<String, DatabaseCollection> collections) {
    this.collections = collections;
  }

  public void addCollection(String collectionName) throws DuplicateCollectionException {
    if (collections.containsKey(collectionName)) {
      throw new DuplicateCollectionException(
          String.format("Cannot create new collection with name [%s]. Collection already existing.", collectionName));
    }

    collections.put(collectionName, new DatabaseCollection(collectionName));
  }

  public void clearCollections() {
    collections.clear();
  }

}
