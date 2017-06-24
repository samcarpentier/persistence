package database.exception.interaction;

import database.exception.DatabaseInteractionException;

public class EntryNotFoundException extends DatabaseInteractionException {

  private static final long serialVersionUID = -3174188842835444221L;

  public EntryNotFoundException(String message) {
    super(message);
  }

}
