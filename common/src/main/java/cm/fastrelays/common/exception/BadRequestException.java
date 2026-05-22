package cm.fastrelays.common.exception;

public class BadRequestException extends RuntimeException {
  public BadRequestException(String errorMessage) {
    super(errorMessage);
  }
}
