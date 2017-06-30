package serialization.manager.component.test;

import java.util.Set;

import com.google.common.collect.Sets;

import serialization.manager.service.SerializableObject;
import serialization.manager.service.annotation.Id;

public class Car implements SerializableObject {

  @Id
  private String vin;

  private String make;
  private String model;
  private int year;
  private Set<String> options = Sets.newHashSet();

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

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public Set<String> getOptions() {
    return options;
  }

  public void addOption(String option) {
    this.options.add(option);
  }

}
