package cm.fastrelays.common.exception;

public class InternalServerError extends RuntimeException {
  public InternalServerError(String errorMessage) {
    super(errorMessage);
  }
}
