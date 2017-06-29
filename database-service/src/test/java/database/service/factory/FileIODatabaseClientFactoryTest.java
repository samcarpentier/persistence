package database.service.factory;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import database.service.*;
import database.service.exception.*;
import serialization.manager.service.SerializationManager;
import serialization.manager.service.exception.DeserializationException;
import util.commons.Assertions;

@RunWith(MockitoJUnitRunner.class)
public class FileIODatabaseClientFactoryTest {

  private static final String DATABASE_NAME = "databaseName";

  private FileIODatabaseClientFactory clientFactory;

  @Mock
  private SerializationManager serializationManager;
  @Mock
  private FileIODatabaseClient databaseClient;

  @Test
  public void givenNoExceptionThrownDuringInstantiation_whenCreate_thenNoExceptionIsThrown() {
    // given
    this.clientFactory = new FileIODatabaseClientFactory(serializationManager) {
      @Override
      protected FileIODatabaseClient instantiate(String databaseName) {
        return databaseClient;
      }
    };

    // when
    clientFactory.create(DATABASE_NAME);

    // then no exception
  }

  @Test(expected = DatabaseClientInstantiationException.class)
  public void givenClientInstantiationThrowsDatabaseLoadingException_whenCreateClient_thenThrowDatabaseClientInstantiationException() {
    // given
    this.clientFactory = new FileIODatabaseClientFactory(serializationManager) {
      @Override
      protected FileIODatabaseClient instantiate(String databaseName) throws DatabaseLoadingException {
        throw new DatabaseLoadingException(null);
      }
    };

    // when
    clientFactory.create(DATABASE_NAME);

    // then
    Assertions.expect(DatabaseClientInstantiationException.class);
  }

  @Test(expected = DatabaseClientInstantiationException.class)
  public void givenClientInstantiationThrowsDeserializationException_whenCreateClient_thenThrowDatabaseClientInstantiationException() {
    // given
    this.clientFactory = new FileIODatabaseClientFactory(serializationManager) {
      @Override
      protected FileIODatabaseClient instantiate(String databaseName) throws DeserializationException {
        throw new DeserializationException(null);
      }
    };
    // when
    clientFactory.create(DATABASE_NAME);

    // then
    Assertions.expect(DatabaseClientInstantiationException.class);
  }

  @Test
  public void givenClientCreated_whenGetClient_thenProxiedClientIsCreated() {
    // given
    this.clientFactory = new FileIODatabaseClientFactory(serializationManager) {
      @Override
      protected FileIODatabaseClient instantiate(String databaseName) {
        return databaseClient;
      }
    };
    clientFactory.create(DATABASE_NAME);

    // when
    FileIODatabaseClient client = clientFactory.getClient();

    // then
    assertThat(client, is(databaseClient));
  }

  @Test(expected = DatabaseClientInstantiationException.class)
  public void givenClientNotCreatedYet_whenGetClient_thenThrowDatabaseClientInstantiationException() {
    // given
    this.clientFactory = new FileIODatabaseClientFactory(serializationManager);

    // when
    clientFactory.getClient();

    // then
    Assertions.expect(DatabaseClientInstantiationException.class);
  }

  @Test
  public void givenClientCreated_whenGetProxiedClient_thenProxiedClientIsCreated() {
    // given
    this.clientFactory = new FileIODatabaseClientFactory(serializationManager) {
      @Override
      protected FileIODatabaseClient instantiate(String databaseName) {
        return databaseClient;
      }
    };
    clientFactory.create(DATABASE_NAME);

    // when
    FileIODatabaseClientLoggingProxy proxiedClient = clientFactory.getProxiedClient();

    // then
    assertThat(proxiedClient, is(not(nullValue())));
  }

  @Test(expected = DatabaseClientInstantiationException.class)
  public void givenClientNotCreatedYet_whenGetProxiedClient_thenThrowDatabaseClientInstantiationException() {
    // given
    this.clientFactory = new FileIODatabaseClientFactory(serializationManager);

    // when
    clientFactory.getProxiedClient();

    // then
    Assertions.expect(DatabaseClientInstantiationException.class);
  }

}
