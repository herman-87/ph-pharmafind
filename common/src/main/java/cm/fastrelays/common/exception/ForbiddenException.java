package cm.fastrelays.common.exception;

public class ForbiddenException extends ApiException {

  public ForbiddenException(String message) {
    super(40301, message);
  }
}
