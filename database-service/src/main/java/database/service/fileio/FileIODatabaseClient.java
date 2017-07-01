package database.service.fileio;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

import database.service.*;
import database.service.exception.*;
import database.service.exception.interaction.*;
import database.service.fileio.exception.*;
import database.service.fileio.model.*;
import serialization.manager.service.*;
import serialization.manager.service.exception.*;
import util.commons.JsonObjectProvider;

public class FileIODatabaseClient implements DatabaseClient {

  private FileIOManager ioManager;
  private SerializationManager serializationManager;

  private Database database;

  public FileIODatabaseClient(String databaseName, FileIOManager ioManager, SerializationManager serializationManager)
      throws DatabaseLoadingException, DeserializationException {
    this.ioManager = ioManager;
    this.serializationManager = serializationManager;
    openDatabase(databaseName, true);
  }

  @Override
  public void openDatabase(String databaseName, boolean createIfAbsent)
      throws DatabaseLoadingException, DeserializationException {
    try {
      this.database = ioManager.loadFromFile(databaseName);
    } catch (DatabaseLoadingException | DeserializationException e) {
      if (createIfAbsent) {
        this.database = createDatabase(databaseName);
      } else {
        throw e;
      }
    }
  }

  private Database createDatabase(String databaseName) {
    Database database = new Database();
    database.setName(databaseName);

    return database;
  }

  @Override
  public void closeDatabase() throws DatabaseSavingException, SerializationException {
    verifyOperationIsAllowed();

    ioManager.writeToFile(database);
    this.database = null;
  }

  @Override
  public DatabaseStatus getDatabaseStatus() {
    if (database == null) {
      return DatabaseStatus.CLOSED;
    }

    return DatabaseStatus.OPENED;
  }

  @Override
  public void createCollection(String collectionName) throws DuplicateCollectionException {
    verifyOperationIsAllowed();

    database.addCollection(collectionName);
  }

  @Override
  public void save(SerializableObject entry, String collectionName)
      throws CollectionNotFoundException, SerializationException, DuplicateIdException {
    verifyOperationIsAllowed();

    DatabaseCollection collection = database.getCollection(collectionName);
    JsonObject collectionEntry = serializationManager.serialize(entry);

    collection.addEntry(collectionEntry);
  }

  @Override
  public SerializableObject findById(String collectionName, Class<? extends SerializableObject> clazz, String id)
      throws CollectionNotFoundException, EntryNotFoundException, DeserializationException {
    verifyOperationIsAllowed();

    DatabaseCollection collection = database.getCollection(collectionName);
    JsonObject desiredEntry = JsonObjectProvider.copy(collection.findEntryById(id));

    return serializationManager.deserialize(desiredEntry, clazz);
  }

  @Override
  public Set<SerializableObject> findByIds(String collectionName, Class<? extends SerializableObject> clazz,
      String... ids) throws EntryNotFoundException, CollectionNotFoundException, DeserializationException {
    verifyOperationIsAllowed();

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
    verifyOperationIsAllowed();

    DatabaseCollection collection = database.getCollection(collectionName);
    collection.removeEntryForId(id);
  }

  @Override
  public void clearCollections() {
    verifyOperationIsAllowed();

    database.clearCollections();
  }

  private void verifyOperationIsAllowed() {
    if (database == null) {
      throw new ClosedDatabaseException("No database is currently opened in the client. Operation unavailable");
    }
  }

}
