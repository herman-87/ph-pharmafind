package cm.fastrelays.common.exception;

public class InvalidEmailAddressException extends ApiException {

  public InvalidEmailAddressException(String message) {
    super(40003, message);
  }
}
