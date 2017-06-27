package database.service;

import java.util.Set;

import database.service.exception.DatabaseSavingException;
import database.service.exception.interaction.*;
import serialization.manager.service.SerializableObject;
import serialization.manager.service.exception.*;

public interface DatabaseClient {

  void closeDatabase() throws DatabaseSavingException, SerializationException;

  void createCollection(String collectionName) throws DuplicateCollectionException;

  void save(SerializableObject entry, String collectionName)
      throws CollectionNotFoundException, SerializationException, DuplicateIdException;

  SerializableObject findById(String collectionName, Class<? extends SerializableObject> clazz, String id)
      throws CollectionNotFoundException, DeserializationException, EntryNotFoundException;

  Set<SerializableObject> findByIds(String collectionName, Class<? extends SerializableObject> clazz, String... ids)
      throws CollectionNotFoundException, EntryNotFoundException, DeserializationException;

  void remove(String collectionName, String id) throws CollectionNotFoundException;

  void clearCollections();

}
