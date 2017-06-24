package database.component.test;

import serialization.manager.service.annotation.Id;

public class MotorCycle {

  @Id
  private String vin;

  private String make;
  private String model;
  private int injectorSize;

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

  public int getInjectorSize() {
    return injectorSize;
  }

  public void setInjectorSize(int injectorSize) {
    this.injectorSize = injectorSize;
  }

}
