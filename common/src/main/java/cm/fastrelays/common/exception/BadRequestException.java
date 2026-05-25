package cm.fastrelays.common.exception;

public class BadRequestException extends ApiException {

  public BadRequestException(String message) {
    super(ErrorCode.VALIDATION_ERROR, message);
  }

  public BadRequestException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
