package serialization.util;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import serialization.SerializableObject;
import serialization.annotation.Id;
import serialization.exception.SerializationException;

public class ObjectFormatProcessor {

  public Field getIdField(Class<? extends SerializableObject> clazz) throws SerializationException {
    List<Field> idFields = FieldUtils.getFieldsListWithAnnotation(clazz, Id.class);

    if (idFields.size() == 0) {
      throw new SerializationException("A serializable object must have a field annotated as ID.");
    } else if (idFields.size() > 1) {
      throw new SerializationException("A serializable object cannot have more than one field annotated as ID.");
    }

    return idFields.get(0);
  }

}
