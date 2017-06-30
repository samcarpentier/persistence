package database.service.exception.interaction;

public class DuplicateCollectionException extends DatabaseInteractionException {

  private static final long serialVersionUID = 3954592416966178458L;

  public DuplicateCollectionException(String message) {
    super(message);
  }

}
