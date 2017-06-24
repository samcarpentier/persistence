package database;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

import database.exception.*;
import database.exception.interaction.*;
import database.model.*;
import serialization.*;
import serialization.exception.*;

public class DatabaseClientImpl implements DatabaseClient {

  private DatabaseFileIOManager ioManager;
  private SerializationManager serializationManager;

  private Database database;

  public DatabaseClientImpl(String databaseName, DatabaseFileIOManager ioManager,
      SerializationManager serializationManager) {
    this.ioManager = ioManager;
    this.serializationManager = serializationManager;
    this.database = resolveDatabase(databaseName);
  }

  private Database resolveDatabase(String databaseName) {
    try {
      return ioManager.loadFromFile(databaseName);
    } catch (DatabaseLoadingException e) {
      Database database = new Database();
      database.setName(databaseName);

      return database;
    }
  }

  @Override
  public void closeDatabase() throws DatabaseSavingException {
    ioManager.writeToFile(database, database.getName());
  }

  @Override
  public void createCollection(String collectionName) throws DuplicateCollectionException {
    database.addCollection(collectionName);
  }

  @Override
  public void save(SerializableObject entry, String collectionName)
      throws CollectionNotFoundException, SerializationException {
    DatabaseCollection collection = database.getCollection(collectionName);

    JsonObject collectionEntry = serializationManager.serialize(entry);
    collection.addEntry(collectionEntry);
  }

  @Override
  public SerializableObject findById(String collectionName, Class<? extends SerializableObject> clazz, String id)
      throws CollectionNotFoundException, EntryNotFoundException, DeserializationException {
    DatabaseCollection collection = database.getCollection(collectionName);
    JsonObject desiredEntry = collection.findEntryById(id);

    return serializationManager.deserialize(desiredEntry, clazz);
  }

  @Override
  public Set<SerializableObject> findByIds(String collectionName, Class<? extends SerializableObject> clazz,
      String... ids) throws EntryNotFoundException, CollectionNotFoundException, DeserializationException {
    DatabaseCollection collection = database.getCollection(collectionName);

    Set<SerializableObject> desiredEntries = Sets.newHashSet();
    for (String id : ids) {
      JsonObject entry = collection.findEntryById(id);
      desiredEntries.add(serializationManager.deserialize(entry, clazz));
    }

    return desiredEntries;
  }

  @Override
  public void remove(String collectionName, String id) throws CollectionNotFoundException {
    DatabaseCollection collection = database.getCollection(collectionName);
    collection.removeEntryForId(id);
  }

}
