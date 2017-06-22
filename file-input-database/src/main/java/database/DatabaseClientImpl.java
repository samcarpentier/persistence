package database;

import com.google.gson.JsonObject;

import database.exception.*;
import database.exception.interaction.DuplicateCollectionException;
import database.model.Database;
import serialization.SerializableObject;

public class DatabaseClientImpl implements DatabaseClient {

  private DatabaseFileIOManager ioManager;
  private Database database;

  public DatabaseClientImpl(DatabaseFileIOManager ioManager) {
    this.ioManager = ioManager;
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
    if (!database.getCollection(collectionName).isPresent()) {
      database.addCollection(collectionName);
    } else {
      throw new DuplicateCollectionException(
          String.format("Cannot create new collection with name [%s]. Collection already existing.", collectionName));
    }
  }

  @Override
  public void save(SerializableObject entry) {
    // TODO Auto-generated method stub
  }

  @Override
  public JsonObject findById(String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonObject findByIds(String... ids) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void remove(String id) {
    // TODO Auto-generated method stub
  }

}
