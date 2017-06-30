package database.service.exception.interaction;

public class DuplicateIdException extends DatabaseInteractionException {

  private static final long serialVersionUID = 664273870522232438L;

  public DuplicateIdException(String message) {
    super(message);
  }

}
