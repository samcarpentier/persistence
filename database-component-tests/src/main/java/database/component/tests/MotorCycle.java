package database.component.tests;

import serialization.manager.service.SerializableObject;
import serialization.manager.service.annotation.Id;

public class MotorCycle implements SerializableObject {

  @Id
  private String vin;

  private String make;
  private String model;
  private int displacement;

  public String getVin() {
    return vin;
  }

  public void setVin(String vin) {
    this.vin = vin;
  }

  public String getMake() {
    return make;
  }

  public void setMake(String make) {
    this.make = make;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public int getDisplacement() {
    return displacement;
  }

  public void setDisplacement(int displacement) {
    this.displacement = displacement;
  }

}
