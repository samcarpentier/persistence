package database;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Paths;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import database.FileInputOutputManager;
import database.model.*;

@RunWith(MockitoJUnitRunner.class)
public class FileInputOutputManagerTest {

  private static final String DATABASE_NAME = "databaseName";
  private static final String COLLECTION_NAME = "collectionName";

  private static final String DATABASE_FILENAME = "database.ser";
  private static final String PATH = "./";

  private FileInputOutputManager fileInputOutputManager;
  private Database inMemoryDatabase;

  @Before
  public void setUp() {
    inMemoryDatabase = new Database();
    inMemoryDatabase.setName(DATABASE_NAME);
    inMemoryDatabase.addCollection(COLLECTION_NAME, new DatabaseCollection());

    this.fileInputOutputManager = new FileInputOutputManager(PATH);
  }

  @Test
  public void givenDatabaseInMemory_whenWriteToFile_thenFileContainsDatabaseContent() throws Exception {
    // when
    fileInputOutputManager.writeToFile(inMemoryDatabase, DATABASE_FILENAME);

    // then
    Database databaseInFile = readDatabaseFromFile();
    assertThat(databaseInFile.getName(), is(DATABASE_NAME));
    assertThat(databaseInFile.getAllCollections().keySet(), contains(COLLECTION_NAME));
    assertThat(databaseInFile.getAllCollections().entrySet(), hasSize(1));
  }

  @Test
  public void givenDatabaseInFile_whenLoad_thenDatabaseInMemoryContainsFileContent() throws Exception {
    // given
    givenDatabaseInFile();

    // when
    Database databaseInFile = fileInputOutputManager.loadFromFile(DATABASE_FILENAME);

    // then
    assertThat(databaseInFile.getName(), is(DATABASE_NAME));
    assertThat(databaseInFile.getAllCollections().keySet(), contains(COLLECTION_NAME));
    assertThat(databaseInFile.getAllCollections().entrySet(), hasSize(1));
  }

  private void givenDatabaseInFile() throws Exception {
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
        new FileOutputStream(Paths.get(PATH, DATABASE_FILENAME).toString()));
    objectOutputStream.writeObject(inMemoryDatabase);
    objectOutputStream.close();
  }

  private Database readDatabaseFromFile() throws Exception {
    ObjectInputStream objectInputStream = new ObjectInputStream(
        new FileInputStream(Paths.get(PATH, DATABASE_FILENAME).toString()));

    Database databaseInFile = (Database) objectInputStream.readObject();
    objectInputStream.close();

    return databaseInFile;
  }

}
