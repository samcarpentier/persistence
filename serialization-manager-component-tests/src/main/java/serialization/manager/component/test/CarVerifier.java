package serialization.manager.component.test;

import org.hamcrest.Matcher;

import com.jayway.jsonassert.JsonAssert;

public class CarVerifier {

  private String json;

  public CarVerifier(String json) {
    this.json = json;
  }

  public CarVerifier assertThatVin(Matcher<String> vinMatcher) {
    JsonAssert.with(json).assertThat("$.vin", vinMatcher);
    return this;
  }

  public CarVerifier assertThatMake(Matcher<String> makeMatcher) {
    JsonAssert.with(json).assertThat("$.make", makeMatcher);
    return this;
  }

  public CarVerifier assertThatModel(Matcher<String> modelMatcher) {
    JsonAssert.with(json).assertThat("$.model", modelMatcher);
    return this;
  }

  public CarVerifier assertThatYear(Matcher<Integer> yearMatcher) {
    JsonAssert.with(json).assertThat("$.year", yearMatcher);
    return this;
  }

  public void assertThatOptions(Matcher<Iterable<? extends String>> optionsMatcher) {
    JsonAssert.with(json).assertThat("$options", optionsMatcher);
  }
}
