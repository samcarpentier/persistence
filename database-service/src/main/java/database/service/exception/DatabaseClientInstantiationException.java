package database.service.exception;

public class DatabaseClientInstantiationException extends RuntimeException {

  private static final long serialVersionUID = -7004472116573257187L;

  public DatabaseClientInstantiationException(String message) {
    super(message);
  }

}
