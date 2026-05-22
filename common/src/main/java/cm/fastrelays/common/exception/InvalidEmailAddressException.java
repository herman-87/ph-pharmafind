package cm.fastrelays.common.exception;

public class InvalidEmailAddressException extends RuntimeException {
  public InvalidEmailAddressException(String errorMessage) {
    super(errorMessage);
  }
}
