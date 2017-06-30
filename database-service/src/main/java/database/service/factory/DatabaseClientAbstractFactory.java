package database.service.factory;

import database.service.DatabaseClient;
import database.service.exception.DatabaseLoadingException;
import serialization.manager.service.SerializationManager;
import serialization.manager.service.exception.DeserializationException;

public abstract class DatabaseClientAbstractFactory<T extends DatabaseClient> {

  protected SerializationManager serializationManager;
  protected T client;

  public DatabaseClientAbstractFactory(SerializationManager serializationManager) {
    this.serializationManager = serializationManager;
  }

  public abstract DatabaseClientAbstractFactory<T> create(String databaseName);

  public abstract DatabaseClient getClient();

  public abstract DatabaseClient getProxiedClient();

  protected abstract T instantiate(String databaseName) throws DatabaseLoadingException, DeserializationException;

}
