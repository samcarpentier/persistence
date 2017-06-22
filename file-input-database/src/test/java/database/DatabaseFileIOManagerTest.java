package database;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Paths;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import database.model.Database;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseFileIOManagerTest {

  private static final String DATABASE_NAME = "databaseName";
  private static final String COLLECTION_NAME = "collectionName";

  private static final String DATABASE_FILENAME_WITH_EXTENSION = "database.ser";
  private static final String DATABASE_FILENAME_WITHOUT_EXTENSION = "database";
  private static final String PATH = "./";

  private DatabaseFileIOManager fileInputOutputManager;
  private Database inMemoryDatabase;

  @Before
  public void setUp() {
    inMemoryDatabase = new Database();
    inMemoryDatabase.setName(DATABASE_NAME);
    inMemoryDatabase.addCollection(COLLECTION_NAME);

    this.fileInputOutputManager = new DatabaseFileIOManager(PATH);
  }

  @Test
  public void givenDatabaseInMemory_whenWriteToFile_thenFileContainsDatabaseContent() throws Exception {
    givenDatabaseInMemoryWithFilename_whenWriteToFile_thenFileContainsDatabase(DATABASE_FILENAME_WITH_EXTENSION);
  }

  @Test
  public void givenDatabaseInMemoryAndDbNameWithoutExtension_whenWriteToFile_thenFileContainsDatabaseContent()
      throws Exception {
    givenDatabaseInMemoryWithFilename_whenWriteToFile_thenFileContainsDatabase(DATABASE_FILENAME_WITHOUT_EXTENSION);
  }

  private void givenDatabaseInMemoryWithFilename_whenWriteToFile_thenFileContainsDatabase(String filename)
      throws Exception {
    // when
    fileInputOutputManager.writeToFile(inMemoryDatabase, filename);

    // then
    Database databaseInFile = readDatabaseFromFile();
    assertThat(databaseInFile.getName(), is(DATABASE_NAME));
    assertThat(databaseInFile.getAllCollections().keySet(), contains(COLLECTION_NAME));
    assertThat(databaseInFile.getAllCollections().entrySet(), hasSize(1));
  }

  @Test
  public void givenDatabaseInFile_whenLoad_thenDatabaseInMemoryContainsFileContent() throws Exception {
    givenDatabaseInFileWithFilename_whenLoad_thenDatabaseInMemoryContainsFileContent(DATABASE_FILENAME_WITH_EXTENSION);
  }

  @Test
  public void givenDatabaseInFileAndDbNameWithoutExtension_whenLoad_thenDatabaseInMemoryContainsFileContent()
      throws Exception {
    givenDatabaseInFileWithFilename_whenLoad_thenDatabaseInMemoryContainsFileContent(
        DATABASE_FILENAME_WITHOUT_EXTENSION);
  }

  private void givenDatabaseInFileWithFilename_whenLoad_thenDatabaseInMemoryContainsFileContent(String filename)
      throws Exception {
    // given
    givenDatabaseInFileWithFilename(filename);

    // when
    Database databaseInFile = fileInputOutputManager.loadFromFile(filename);

    // then
    assertThat(databaseInFile.getName(), is(DATABASE_NAME));
    assertThat(databaseInFile.getAllCollections().keySet(), contains(COLLECTION_NAME));
    assertThat(databaseInFile.getAllCollections().entrySet(), hasSize(1));
  }

  private void givenDatabaseInFileWithFilename(String filename) throws Exception {
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
        new FileOutputStream(Paths.get(PATH, filename).toString()));
    objectOutputStream.writeObject(inMemoryDatabase);
    objectOutputStream.close();
  }

  private Database readDatabaseFromFile() throws Exception {
    ObjectInputStream objectInputStream = new ObjectInputStream(
        new FileInputStream(Paths.get(PATH, DATABASE_FILENAME_WITH_EXTENSION).toString()));

    Database databaseInFile = (Database) objectInputStream.readObject();
    objectInputStream.close();

    return databaseInFile;
  }

}
