package cm.fastrelays.common.exception;

public record ErrorCode(String code, String description) {

  public static final ErrorCode VALIDATION_ERROR = new ErrorCode("400_001", "Request validation failed");
  public static final ErrorCode INVALID_ARGUMENT = new ErrorCode("400_002", "Invalid argument provided");
  public static final ErrorCode INVALID_EMAIL = new ErrorCode("400_003", "Invalid email address format");
  public static final ErrorCode PASSWORDS_MISMATCH = new ErrorCode("400_004", "Passwords do not match");
  public static final ErrorCode MISSING_PARAMETER = new ErrorCode("400_005", "Missing request parameter");
  public static final ErrorCode MALFORMED_JSON = new ErrorCode("400_006", "Malformed JSON request");

  public static final ErrorCode ACCESS_DENIED = new ErrorCode("403_001", "Access denied");

  public static final ErrorCode USER_NOT_FOUND = new ErrorCode("404_001", "User not found");
  public static final ErrorCode RESOURCE_NOT_FOUND = new ErrorCode("404_002", "Resource not found");

  public static final ErrorCode EMAIL_ALREADY_EXISTS = new ErrorCode("409_001", "Email already exists");
  public static final ErrorCode PHONE_ALREADY_EXISTS = new ErrorCode("409_002", "Phone number already exists");
  public static final ErrorCode USERNAME_ALREADY_EXISTS = new ErrorCode("409_003", "Username already exists");
  public static final ErrorCode CONFLICT = new ErrorCode("409_004", "Conflict detected");

  public static final ErrorCode INTERNAL_ERROR = new ErrorCode("500_001", "Internal server error");
  public static final ErrorCode EXTERNAL_SERVICE_ERROR = new ErrorCode("502_001", "External service error");
}
