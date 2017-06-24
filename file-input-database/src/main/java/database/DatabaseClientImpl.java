package database;

import java.util.Set;

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

  public DatabaseClientImpl(DatabaseFileIOManager ioManager, SerializationManager serializationManager) {
    this.ioManager = ioManager;
    this.serializationManager = serializationManager;
  }

  @Override
  public void openDatabase(String databaseName) throws DatabaseLoadingException {
    this.database = ioManager.loadFromFile(databaseName);
  }

  @Override
  public void closeDatabase() throws DatabaseSavingException {
    if (database != null) {
      ioManager.writeToFile(database, database.getName());
    }
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
  public SerializableObject findById(String collectionName, String id, Class<? extends SerializableObject> clazz)
      throws CollectionNotFoundException, EntryNotFoundException, DeserializationException {
    DatabaseCollection collection = database.getCollection(collectionName);
    JsonObject desiredEntry = collection.findEntryById(id);

    return serializationManager.deserialize(desiredEntry, clazz);
  }

  @Override
  public Set<SerializableObject> findByIds(String collectionName, String... ids) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void remove(String collectionName, String id) {
    // TODO Auto-generated method stub
  }

}
