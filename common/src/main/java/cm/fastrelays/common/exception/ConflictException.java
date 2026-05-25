package cm.fastrelays.common.exception;

public class ConflictException extends ApiException {

  public ConflictException(String message) {
    super(40904, message);
  }

  public ConflictException(int errorCode, String message) {
    super(errorCode, message);
  }
}
