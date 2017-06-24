package database;

import java.util.Set;

import database.exception.DatabaseSavingException;
import database.exception.interaction.*;
import serialization.SerializableObject;
import serialization.exception.*;

public interface DatabaseClient {

  void closeDatabase() throws DatabaseSavingException;

  void createCollection(String collectionName) throws DuplicateCollectionException;

  void save(SerializableObject entry, String collectionName) throws CollectionNotFoundException, SerializationException;

  SerializableObject findById(String collectionName, Class<? extends SerializableObject> clazz, String id)
      throws CollectionNotFoundException, DeserializationException, EntryNotFoundException;

  Set<SerializableObject> findByIds(String collectionName, Class<? extends SerializableObject> clazz, String... ids)
      throws CollectionNotFoundException, EntryNotFoundException, DeserializationException;

  void remove(String collectionName, String id) throws CollectionNotFoundException;

}
