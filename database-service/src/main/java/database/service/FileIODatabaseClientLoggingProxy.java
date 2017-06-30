package database.service;

import java.util.*;
import java.util.logging.Logger;

import database.service.exception.*;
import database.service.exception.interaction.*;
import serialization.manager.service.SerializableObject;
import serialization.manager.service.exception.*;

public class FileIODatabaseClientLoggingProxy implements DatabaseClient {

  private static final Logger logger = Logger.getLogger(FileIODatabaseClientLoggingProxy.class.getName());

  private FileIODatabaseClient databaseClient;

  public FileIODatabaseClientLoggingProxy(FileIODatabaseClient databaseClient) {
    this.databaseClient = databaseClient;
  }

  @Override
  public void openDatabase(String databaseName, boolean createIfAbsent)
      throws DatabaseLoadingException, DeserializationException {
    logger
        .info(String.format("Attempting to open database [%s]. Create if absent: [%s].", databaseName, createIfAbsent));

    try {
      databaseClient.openDatabase(databaseName, createIfAbsent);
      logger.info(String.format("Database successfully opened"));
    } catch (Exception e) {
      logError(e);
      throw e;
    }
  }

  @Override
  public void closeDatabase() throws DatabaseSavingException, SerializationException {
    logger.info(String.format("Attempting to close database."));

    try {
      databaseClient.closeDatabase();
      logger.info(String.format("Database successfully closed."));
    } catch (Exception e) {
      logError(e);
      throw e;
    }
  }

  @Override
  public DatabaseStatus getDatabaseStatus() {
    logger.info(String.format("Getting database status"));

    DatabaseStatus status = databaseClient.getDatabaseStatus();
    logger.info(String.format("Database is [%s]", status.name()));

    return status;
  }

  @Override
  public void createCollection(String collectionName) throws DuplicateCollectionException {
    logger.info(String.format("Attempting to create collection [%s].", collectionName));

    try {
      databaseClient.createCollection(collectionName);
      logger.info(String.format("Collection successfully created."));
    } catch (Exception e) {
      logError(e);
      throw e;
    }
  }

  @Override
  public void save(SerializableObject entry, String collectionName)
      throws CollectionNotFoundException, SerializationException, DuplicateIdException {
    logger.info(String.format("Attempting to save entry [%s] in collection [%s].",
        entry.getClass().getSimpleName(),
        collectionName));

    try {
      databaseClient.save(entry, collectionName);
      logger.info(String.format("Entry successfully saved."));
    } catch (Exception e) {
      logError(e);
      throw e;
    }
  }

  @Override
  public SerializableObject findById(String collectionName, Class<? extends SerializableObject> clazz, String id)
      throws CollectionNotFoundException, DeserializationException, EntryNotFoundException {
    logger.info(String.format("Attempting to find entry with ID [%s] in collection [%s].", id, collectionName));

    try {
      SerializableObject retrievedEntry = databaseClient.findById(collectionName, clazz, id);
      logger.info(String.format("Successfully found entry of class [%s].", clazz));

      return retrievedEntry;
    } catch (Exception e) {
      logError(e);
      throw e;
    }
  }

  @Override
  public Set<SerializableObject> findByIds(String collectionName, Class<? extends SerializableObject> clazz,
      String... ids) throws CollectionNotFoundException, EntryNotFoundException, DeserializationException {
    logger.info(String.format("Attempting to find entries with IDs %s in collection [%s].",
        Arrays.toString(ids),
        collectionName));

    try {
      Set<SerializableObject> retrivedEntries = databaseClient.findByIds(collectionName, clazz, ids);
      logger.info(String.format("Successfully found %d entries of class [%s].", retrivedEntries.size(), clazz));

      return retrivedEntries;
    } catch (Exception e) {
      logError(e);
      throw e;
    }
  }

  @Override
  public void remove(String collectionName, String id) throws CollectionNotFoundException {
    logger.info(String.format("Attempting to remove entry with ID [%s] in collection [%s].", id, collectionName));

    try {
      databaseClient.remove(collectionName, id);
      logger.info(String.format("Successfully removed entry with ID [%s].", id));
    } catch (Exception e) {
      logError(e);
      throw e;
    }
  }

  @Override
  public void clearCollections() {
    logger.info(String.format("Attempting to clear database collections"));
    databaseClient.clearCollections();
    logger.info(String.format("Successfully cleared database collections"));
  }

  private void logError(Throwable e) {
    logger.warning(String.format("An error occured: %s", e.getMessage()));
  }

}
