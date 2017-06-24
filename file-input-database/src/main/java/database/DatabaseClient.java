package database;

import java.util.Set;

import database.exception.*;
import database.exception.interaction.*;
import serialization.SerializableObject;
import serialization.exception.*;

public interface DatabaseClient {

  void openDatabase(String databaseName) throws DatabaseLoadingException;

  void closeDatabase() throws DatabaseSavingException;

  void createCollection(String collectionName) throws DuplicateCollectionException;

  void save(SerializableObject entry, String collectionName) throws CollectionNotFoundException, SerializationException;

  SerializableObject findById(String collectionName, String id, Class<? extends SerializableObject> clazz)
      throws CollectionNotFoundException, DeserializationException, EntryNotFoundException;

  Set<SerializableObject> findByIds(String collectionName, String... ids);

  void remove(String collectionName, String id);

}
