package util.commons;

import static org.junit.Assert.*;

public class Assertions {

  public static void expect(Class<? extends Exception> e) {
    String name = e.getSimpleName();
    fail(String.format("Expected %s but was never thrown.", name));
  }
}
