package com.ph.pharmafind;

public final class ErrorCode {

  public static final int VALIDATION_ERROR = 40001;
  public static final int INVALID_EMAIL_FORMAT = 40002;
  public static final int INVALID_PHONE_FORMAT = 40003;
  public static final int PASSWORD_MISMATCH = 40004;
  public static final int WEAK_PASSWORD = 40005;
  public static final int INVALID_TOKEN = 40006;
  public static final int TOKEN_EXPIRED = 40007;

  public static final int ACCESS_DENIED = 40301;

  public static final int PHARMACY_NOT_FOUND = 40401;

  public static final int EMAIL_ALREADY_EXISTS = 40901;
  public static final int PHONE_ALREADY_EXISTS = 40902;
  public static final int PHARMACY_NAME_ALREADY_EXISTS = 40903;

  private ErrorCode() {}
}
