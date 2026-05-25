package cm.fastrelays.common.exception;

public class InvalidEmailAddressException extends ApiException {

  public InvalidEmailAddressException(String message) {
    super(ErrorCode.INVALID_EMAIL, message);
  }
}
