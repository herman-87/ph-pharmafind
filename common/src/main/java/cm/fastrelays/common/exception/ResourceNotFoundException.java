package cm.fastrelays.common.exception;

public class ResourceNotFoundException extends ApiException {

  public ResourceNotFoundException(String message) {
    super(40402, message);
  }

  public ResourceNotFoundException(int errorCode, String message) {
    super(errorCode, message);
  }
}
