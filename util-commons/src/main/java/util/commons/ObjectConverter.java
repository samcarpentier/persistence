package util.commons;

import java.util.Set;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ObjectConverter<D, S> {

  public D convert(S source) {
    return (D) source;
  }

  public Set<D> convertAll(Set<S> sources) {
    Set<D> converted = (Set) sources;
    return converted;
  }

}
