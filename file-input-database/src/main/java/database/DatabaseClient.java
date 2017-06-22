package database;

import com.google.gson.JsonObject;

import database.exception.*;
import database.exception.interaction.DuplicateCollectionException;
import serialization.SerializableObject;

public interface DatabaseClient {

  void openDatabase(String databaseName) throws DatabaseLoadingException;

  void closeDatabase() throws DatabaseSavingException;

  void createCollection(String collectionName) throws DuplicateCollectionException;

  void save(SerializableObject entry);

  JsonObject findById(String id);

  JsonObject findByIds(String... ids);

  void remove(String id);

}
