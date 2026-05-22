package cm.fastrelays.common.exception;

public class ForbiddenException extends RuntimeException {
  public ForbiddenException(String errorMessage) {
    super(errorMessage);
  }
}
