package cm.fastrelays.common.exception;

public class ExternalServiceCallException extends ApiException {

  public ExternalServiceCallException(String message) {
    super(50201, message);
  }
}
