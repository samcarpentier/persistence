package database.service;

import serialization.manager.service.*;
import serialization.manager.service.codec.Codec;

public class DatabaseClientFactory {

  public DatabaseClient createClient(DatabaseType databaseType, String databaseName) {
    if (databaseType == DatabaseType.FILE_IO_DB) {
      SerializationManager serializationManager = new SerializationManager(new Codec(new ObjectFormatProcessor()));
      return new FileIODatabaseClient(databaseName, new FileIOManager(serializationManager), serializationManager);
    }

    throw new IllegalArgumentException(String.format("No database client available for type [%s].",
        databaseType != null ? databaseType.name() : "<null>"));
  }

}
