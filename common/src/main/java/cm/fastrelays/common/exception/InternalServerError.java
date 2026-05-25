package cm.fastrelays.common.exception;

public class InternalServerError extends ApiException {

  public InternalServerError(String message) {
    super(ErrorCode.INTERNAL_ERROR, message);
  }

  public InternalServerError(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
