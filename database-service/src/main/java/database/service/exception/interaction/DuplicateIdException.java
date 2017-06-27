package database.service.exception.interaction;

import database.service.exception.DatabaseInteractionException;

public class DuplicateIdException extends DatabaseInteractionException {

  private static final long serialVersionUID = 664273870522232438L;

  public DuplicateIdException(String message) {
    super(message);
  }

}
