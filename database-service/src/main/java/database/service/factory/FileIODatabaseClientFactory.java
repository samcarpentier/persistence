package database.service.factory;

import database.service.*;
import database.service.exception.*;
import serialization.manager.service.SerializationManager;
import serialization.manager.service.exception.DeserializationException;

public class FileIODatabaseClientFactory extends DatabaseClientAbstractFactory<FileIODatabaseClient> {

  public FileIODatabaseClientFactory(SerializationManager serializationManager) {
    super(serializationManager);
  }

  @Override
  public FileIODatabaseClientFactory create(String databaseName) {
    try {
      this.client = instantiate(databaseName);
      return this;
    } catch (DatabaseLoadingException | DeserializationException e) {
      throw new DatabaseClientInstantiationException(
          String.format("Error while instantiating database client [%s] with database name [%s]",
              FileIODatabaseClient.class,
              databaseName));
    }
  }

  @Override
  public FileIODatabaseClientLoggingProxy getProxiedClient() {
    if (client == null) {
      throw new DatabaseClientInstantiationException(
          String.format("The client must be instantiated prior to accessing the proxied client."));
    }

    return new FileIODatabaseClientLoggingProxy(client);
  }

  @Override
  public FileIODatabaseClient getClient() {
    if (client == null) {
      throw new DatabaseClientInstantiationException(
          String.format("The client must be created prior to  accessing its instance."));
    }

    return client;
  }

  @Override
  protected FileIODatabaseClient instantiate(String databaseName)
      throws DatabaseLoadingException, DeserializationException {
    return new FileIODatabaseClient(databaseName, new FileIOManager(serializationManager), serializationManager);
  }

}
