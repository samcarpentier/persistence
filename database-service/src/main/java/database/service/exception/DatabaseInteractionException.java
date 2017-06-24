package database.service.exception;

public abstract class DatabaseInteractionException extends Exception {

  private static final long serialVersionUID = -3005454709259429900L;

  protected DatabaseInteractionException(String message) {
    super(message);
  }

}
