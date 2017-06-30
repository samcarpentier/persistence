package database.service.exception;

public class ClosedDatabaseException extends RuntimeException {

  private static final long serialVersionUID = 4763764546209759847L;

  public ClosedDatabaseException(String message) {
    super(message);
  }

}
