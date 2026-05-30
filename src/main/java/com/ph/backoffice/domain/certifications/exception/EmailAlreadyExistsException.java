package com.ph.backoffice.domain.certifications.exception;

public class EmailAlreadyExistsException extends DomainException {
  public EmailAlreadyExistsException() {
    super(PharmacyErrorCode.PHARMACY_ERR_003);
  }

  public EmailAlreadyExistsException(String message) {
    super(PharmacyErrorCode.PHARMACY_ERR_003, message);
  }
}
