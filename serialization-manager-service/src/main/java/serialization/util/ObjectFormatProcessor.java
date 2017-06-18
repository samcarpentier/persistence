package serialization.util;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import serialization.SerializableObject;
import serialization.annotation.Id;
import serialization.exception.*;

public class ObjectFormatProcessor {

  private static final boolean ACCESS_PROTECTED_FIELDS = true;

  public List<Field> getFields(Class<? extends SerializableObject> clazz) {
    return FieldUtils.getAllFieldsList(clazz);
  }

  public Field getIdField(Class<? extends SerializableObject> clazz) throws SerializationException {
    List<Field> idFields = FieldUtils.getFieldsListWithAnnotation(clazz, Id.class);

    if (idFields.size() > 1) {
      throw new SerializationException("A serializable object cannot have more than one field annotated as Id.");
    }

    return idFields.get(0);
  }

  public void writeValueInField(SerializableObject object, String fieldName, Object value)
      throws DeserializationException {
    try {
      FieldUtils.writeDeclaredField(object, fieldName, value, ACCESS_PROTECTED_FIELDS);
    } catch (IllegalAccessException e) {
      throw new DeserializationException(String.format("Could not assign value to field [%s]", fieldName));
    }
  }

}
