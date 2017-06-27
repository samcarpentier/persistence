package database.service;

import java.util.Set;
import java.util.logging.Logger;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

import database.service.exception.*;
import database.service.exception.interaction.*;
import database.service.model.*;
import serialization.manager.service.*;
import serialization.manager.service.exception.*;
import util.commons.*;

public class FileIODatabaseClient implements DatabaseClient {

  private static final Logger logger = Logger.getLogger(FileIODatabaseClient.class.getName());

  private FileIOManager ioManager;
  private SerializationManager serializationManager;

  private Database database;

  public FileIODatabaseClient(String databaseName, FileIOManager ioManager, SerializationManager serializationManager) {
    this.ioManager = ioManager;
    this.serializationManager = serializationManager;
    this.database = resolveDatabase(databaseName);
  }

  private Database resolveDatabase(String databaseName) {
    try {
      Database loadedDatabase = ioManager.loadFromFile(databaseName);
      logger.info(String.format("Loaded database [%s] from file.", databaseName));

      return loadedDatabase;
    } catch (DatabaseLoadingException | DeserializationException e) {
      logger.info(String.format("No existing database found for name [%s]. Creating new instance.", databaseName));
      Database database = new Database();
      database.setName(databaseName);

      return database;
    }
  }

  @Override
  public void closeDatabase() throws DatabaseSavingException, SerializationException {
    ioManager.writeToFile(database);
  }

  @Override
  public void createCollection(String collectionName) throws DuplicateCollectionException {
    database.addCollection(collectionName);
    logger.info(String.format("Successfully created collection [%s].", collectionName));
  }

  @Override
  public void save(SerializableObject entry, String collectionName)
      throws CollectionNotFoundException, SerializationException, DuplicateIdException {
    DatabaseCollection collection = database.getCollection(collectionName);

    JsonObject collectionEntry = serializationManager.serialize(entry);
    collection.addEntry(collectionEntry);

    logger.info(String.format("Successfully saved [%s] with ID [%s] in collection [%s]",
        entry.getClass().getName(),
        collectionEntry.get(collectionEntry.get(PersistenceConfig.ID_FIELD_IDENTIFIER).getAsString()),
        collectionName));
  }

  @Override
  public SerializableObject findById(String collectionName, Class<? extends SerializableObject> clazz, String id)
      throws CollectionNotFoundException, EntryNotFoundException, DeserializationException {
    System.out.println(database.getCollection(collectionName).getEntries());

    DatabaseCollection collection = database.getCollection(collectionName);

    logger.info(String.format("Attempting to retrieve [%s] with ID [%s] in collection [%s]",
        clazz.getName(),
        id,
        collectionName));

    JsonObject desiredEntry = JsonObjectProvider.copy(collection.findEntryById(id));
    return serializationManager.deserialize(desiredEntry, clazz);
  }

  @Override
  public Set<SerializableObject> findByIds(String collectionName, Class<? extends SerializableObject> clazz,
      String... ids) throws EntryNotFoundException, CollectionNotFoundException, DeserializationException {
    DatabaseCollection collection = database.getCollection(collectionName);

    Set<SerializableObject> desiredEntries = Sets.newHashSet();
    for (String id : ids) {
      JsonObject entry = JsonObjectProvider.copy(collection.findEntryById(id));
      desiredEntries.add(serializationManager.deserialize(entry, clazz));
    }

    return desiredEntries;
  }

  @Override
  public void remove(String collectionName, String id) throws CollectionNotFoundException {
    DatabaseCollection collection = database.getCollection(collectionName);
    collection.removeEntryForId(id);
  }

  @Override
  public void clearCollections() {
    database.clearCollections();
  }

}
