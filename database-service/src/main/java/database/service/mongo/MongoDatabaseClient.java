package database.service.mongo;

import java.util.Set;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

import database.service.*;
import database.service.exception.interaction.*;
import database.service.fileio.exception.*;
import serialization.manager.service.SerializableObject;
import serialization.manager.service.exception.*;

public class MongoDatabaseClient implements DatabaseClient {

  private MongoClient mongoClient;

  private MongoDatabase database;

  public MongoDatabaseClient(String databaseName, MongoClient mongoClient)
      throws DatabaseLoadingException, DeserializationException {
    this.mongoClient = mongoClient;
    openDatabase(databaseName, true);
  }

  @Override
  public void openDatabase(String databaseName, boolean createIfAbsent)
      throws DatabaseLoadingException, DeserializationException {
    database = mongoClient.getDatabase(databaseName);
  }

  @Override
  public void closeDatabase() throws DatabaseSavingException, SerializationException {
    database = null;
  }

  @Override
  public DatabaseStatus getDatabaseStatus() {
    if (database != null) {
      return DatabaseStatus.OPENED;
    }

    return DatabaseStatus.CLOSED;
  }

  @Override
  public void createCollection(String collectionName) throws DuplicateCollectionException {
    try {
      database.createCollection(collectionName);
    } catch (MongoCommandException e) {
      throw new DuplicateCollectionException(
          String.format("Cannot create new collection with name [%s]. Collection already existing.", collectionName));
    }
  }

  @Override
  public void save(SerializableObject entry, String collectionName)
      throws CollectionNotFoundException, SerializationException, DuplicateIdException {
  }

  @Override
  public SerializableObject findById(String collectionName, Class<? extends SerializableObject> clazz, String id)
      throws CollectionNotFoundException, DeserializationException, EntryNotFoundException {
    return null;
  }

  @Override
  public Set<SerializableObject> findByIds(String collectionName, Class<? extends SerializableObject> clazz,
      String... ids) throws CollectionNotFoundException, EntryNotFoundException, DeserializationException {
    return null;
  }

  @Override
  public void remove(String collectionName, String id) throws CollectionNotFoundException {
  }

  @Override
  public void clearCollections() {
    database.drop();
  }

  // For testing purposes
  protected MongoDatabase getDatabase() {
    return database;
  }

}
