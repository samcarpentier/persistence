package serialization.manager.component.test;

import serialization.manager.service.SerializableObject;

public class ObjectWithoutId implements SerializableObject {

  private String param1;

  public String getParam1() {
    return param1;
  }

  public void setParam1(String param1) {
    this.param1 = param1;
  }

}
