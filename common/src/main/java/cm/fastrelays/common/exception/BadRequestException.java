package cm.fastrelays.common.exception;

public class BadRequestException extends ApiException {

  public BadRequestException(String message) {
    super(40001, message);
  }

  public BadRequestException(int errorCode, String message) {
    super(errorCode, message);
  }
}
