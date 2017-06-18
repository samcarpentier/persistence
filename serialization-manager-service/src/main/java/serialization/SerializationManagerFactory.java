package serialization;

import serialization.codec.Codec;
import serialization.util.ObjectFormatProcessor;

public class SerializationManagerFactory {

  public SerializationManager create() {
    return new SerializationManager(new Codec(new ObjectFormatProcessor()));
  }

}
