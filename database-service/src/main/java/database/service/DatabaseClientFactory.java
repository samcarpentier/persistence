package database.service;

import serialization.manager.service.*;
import serialization.manager.service.codec.Codec;
import util.commons.PersistenceConfig;

public class DatabaseClientFactory {

  public DatabaseClient createClient(DatabaseType databaseType, String databaseName) {
    if (databaseType == DatabaseType.FILE_IO_DB) {
      return new FileIODatabaseClient(databaseName,
          new DatabaseFileIOManager(PersistenceConfig.DEFAULT_DATABASE_LOCATION),
          new SerializationManager(new Codec(new ObjectFormatProcessor())));
    }

    throw new IllegalArgumentException(String.format("No database client available for type [%s].",
        databaseType != null ? databaseType.name() : "<null>"));
  }

}