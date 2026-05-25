package cm.fastrelays.common.exception;

import lombok.Getter;

@Getter
public abstract class ApiException extends RuntimeException {

  private final int errorCode;

  protected ApiException(int errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  protected ApiException(int errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }
}
