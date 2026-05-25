package cm.fastrelays.common.exception;

public class ExternalServiceCallException extends ApiException {

  public ExternalServiceCallException(String message) {
    super(ErrorCode.EXTERNAL_SERVICE_ERROR, message);
  }
}
