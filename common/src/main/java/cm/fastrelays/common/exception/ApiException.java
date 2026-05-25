package cm.fastrelays.common.exception;

import lombok.Getter;

@Getter
public abstract class ApiException extends RuntimeException {

  private final ErrorCode errorCode;

  protected ApiException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  protected ApiException(ErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }
}
