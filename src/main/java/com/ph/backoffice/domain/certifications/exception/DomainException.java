package com.ph.backoffice.domain.certifications.exception;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {
  private final PharmacyErrorCode errorCode;

  protected DomainException(PharmacyErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  protected DomainException(PharmacyErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}
