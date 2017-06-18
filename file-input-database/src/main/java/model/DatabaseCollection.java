package model;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

public class DatabaseCollection {

  private String name;
  private Set<JsonObject> entries = Sets.newHashSet();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<JsonObject> getEntries() {
    return entries;
  }

  public void setEntries(Set<JsonObject> entries) {
    this.entries = entries;
  }

}
