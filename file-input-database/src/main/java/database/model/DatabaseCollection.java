package database.model;

import java.io.Serializable;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

public class DatabaseCollection implements Serializable {

  private static final long serialVersionUID = -6466603262313845430L;

  private Set<JsonObject> entries = Sets.newHashSet();

  public Set<JsonObject> getEntries() {
    return entries;
  }

  public void setEntries(Set<JsonObject> entries) {
    this.entries = entries;
  }

}
