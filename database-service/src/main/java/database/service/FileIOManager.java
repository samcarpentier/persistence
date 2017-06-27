package database.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import database.service.exception.*;
import database.service.model.Database;
import serialization.manager.service.SerializationManager;
import serialization.manager.service.exception.*;
import util.commons.*;

public class FileIOManager {

  private SerializationManager serializationManager;

  public FileIOManager(SerializationManager serializationManager) {
    this.serializationManager = serializationManager;
  }

  public void writeToFile(Database database) throws SerializationException, DatabaseSavingException {
    Path path = buildDatabaseIOPath(database.getName());
    JsonObject serializedDatabase = serializationManager.serialize(database);

    try {
      Files.write(path,
          serializedDatabase.toString().getBytes(),
          StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      throw new DatabaseSavingException(
          String.format("Cannot write database [%s] to file [%s].", database.getName(), path.toString()));
    }
  }

  private Path buildDatabaseIOPath(String databaseName) {
    return Paths.get(PersistenceConfig.DEFAULT_DATABASE_LOCATION,
        databaseName + PersistenceConfig.SERIALIZED_FILE_EXTENSION);
  }

  public Database loadFromFile(String databaseName) throws DatabaseLoadingException, DeserializationException {
    Path path = buildDatabaseIOPath(databaseName);
    String serializedDatabase = null;

    try {
      serializedDatabase = Files.lines(path).collect(Collectors.toList()).get(0);
    } catch (IOException e) {
      throw new DatabaseLoadingException(
          String.format("Cannot lead database [%s] from file [%s].", databaseName, path.toString()));
    }

    return (Database) serializationManager.deserialize(JsonObjectProvider.fromString(serializedDatabase),
        Database.class);
  }

}
