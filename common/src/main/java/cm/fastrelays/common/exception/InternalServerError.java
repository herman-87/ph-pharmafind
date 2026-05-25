package cm.fastrelays.common.exception;

public class InternalServerError extends ApiException {

  public InternalServerError(String message) {
    super(50001, message);
  }

  public InternalServerError(int errorCode, String message) {
    super(errorCode, message);
  }
}
