package util.commons;

import static org.junit.Assert.*;

import java.nio.file.*;
import java.util.logging.Logger;

public class TestUtils {

  private static final Logger logger = Logger.getLogger(TestUtils.class.getName());

  public static void expect(Class<? extends Exception> e) {
    String name = e.getSimpleName();
    fail(String.format("Expected %s but was never thrown.", name));
  }

  public static void cleanupDatabaseFiles(String databaseName) {
    try {
      Files.deleteIfExists(Paths.get(PersistenceConfig.DEFAULT_DATABASE_LOCATION,
          databaseName + PersistenceConfig.SERIALIZED_FILE_EXTENSION));

      Files.deleteIfExists(Paths.get(PersistenceConfig.DEFAULT_DATABASE_LOCATION));
    } catch (Exception e) {
      logger.warning(String.format("An unexpected error occurred while <cleanupDatabaseFiles>: %s", e.getMessage()));
    }
  }
}
