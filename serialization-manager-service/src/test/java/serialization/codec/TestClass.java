package serialization.codec;

import serialization.SerializableObject;
import serialization.annotation.Id;

public class TestClass implements SerializableObject {

  @Id
  private String arg1;
  private String arg2;
  private int val1;

  public TestClass() {
    // For deserialization
  }

  public String getArg1() {
    return arg1;
  }

  public void setArg1(String arg1) {
    this.arg1 = arg1;
  }

  public String getArg2() {
    return arg2;
  }

  public void setArg2(String arg2) {
    this.arg2 = arg2;
  }

  public int getVal1() {
    return val1;
  }

  public void setVal1(int val1) {
    this.val1 = val1;
  }

  @Override
  public String toString() {
    return "TestClass [arg1=" + arg1 + ", arg2=" + arg2 + ", val1=" + val1 + "]";
  }

}
