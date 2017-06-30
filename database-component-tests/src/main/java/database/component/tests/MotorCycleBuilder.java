package database.component.tests;

public class MotorCycleBuilder {

  private String vin;
  private String make;
  private String model;
  private int displacement;

  public MotorCycleBuilder withVin(String vin) {
    this.vin = vin;
    return this;
  }

  public MotorCycleBuilder withMake(String make) {
    this.make = make;
    return this;
  }

  public MotorCycleBuilder withModel(String model) {
    this.model = model;
    return this;
  }

  public MotorCycleBuilder withDisplacement(int displacement) {
    this.displacement = displacement;
    return this;
  }

  public MotorCycle build() {
    MotorCycle motorCycle = new MotorCycle();

    motorCycle.setVin(vin);
    motorCycle.setMake(make);
    motorCycle.setModel(model);
    motorCycle.setDisplacement(displacement);

    return motorCycle;
  }

}
