package util.commons;

import java.nio.file.Paths;

public class PersistenceConfig {

  public static final String ID_FIELD_IDENTIFIER = "id";

  public static final String DEFAULT_DATABASE_LOCATION = Paths.get(System.getProperty("user.home"), "/data").toString();
  public static final String SERIALIZED_FILE_EXTENSION = ".per";

}
