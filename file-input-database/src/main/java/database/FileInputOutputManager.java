package database;

import java.io.*;
import java.nio.file.Paths;

import database.exception.*;
import database.model.Database;

public class FileInputOutputManager {

  private String path;

  private ObjectOutputStream objectOutputStream;
  private ObjectInputStream objectInputStream;

  public FileInputOutputManager(String path) {
    this.path = path;
  }

  public void writeToFile(Database inMemoryDatabase, String filename) throws DatabaseSavingException {
    String pathToFile = buildPathToDatabase(path, filename);

    try {
      File file = new File(pathToFile);
      file.createNewFile();

      objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
      objectOutputStream.writeObject(inMemoryDatabase);
    } catch (IOException e) {
      throw new DatabaseSavingException(String.format("Unable to write in-memory database to file [%s].", pathToFile));
    }
  }

  public Database loadFromFile(String filename) throws DatabaseLoadingException {
    String pathToFile = buildPathToDatabase(path, filename);

    try {
      objectInputStream = new ObjectInputStream(new FileInputStream(pathToFile));
      return (Database) objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new DatabaseLoadingException(
          String.format("Unable to load database from specified file [%s].", pathToFile));
    }
  }

  private String buildPathToDatabase(String path, String filename) {
    return Paths.get(path, filename).toString();
  }

}
