package serialization.manager.service;

import serialization.manager.service.codec.Codec;

public class SerializationManagerFactory {

  public SerializationManager create() {
    return new SerializationManager(new Codec(new ObjectFormatProcessor()));
  }

}
