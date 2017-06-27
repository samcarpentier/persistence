package database.component.test;

import static org.junit.Assert.*;

import org.hamcrest.Matcher;

public class MotorCycleVerifier {

  private MotorCycle motorCycle;

  public MotorCycleVerifier(MotorCycle motorCycle) {
    this.motorCycle = motorCycle;
  }

  public MotorCycleVerifier assertThatVin(Matcher<String> vinMatcher) {
    assertThat(motorCycle.getVin(), vinMatcher);
    return this;
  }

  public MotorCycleVerifier assertThatMake(Matcher<String> makeMatcher) {
    assertThat(motorCycle.getMake(), makeMatcher);
    return this;
  }

  public MotorCycleVerifier assertThatModel(Matcher<String> modelMatcher) {
    assertThat(motorCycle.getModel(), modelMatcher);
    return this;
  }

  public MotorCycleVerifier assertThatDisplacement(Matcher<Integer> displacementMatcher) {
    assertThat(motorCycle.getDisplacement(), displacementMatcher);
    return this;
  }

}
