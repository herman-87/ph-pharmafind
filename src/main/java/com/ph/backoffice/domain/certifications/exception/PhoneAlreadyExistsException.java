package com.ph.backoffice.domain.certifications.exception;

public class PhoneAlreadyExistsException extends DomainException {
  public PhoneAlreadyExistsException() {
    super(PharmacyErrorCode.PHARMACY_ERR_004);
  }

  public PhoneAlreadyExistsException(String message) {
    super(PharmacyErrorCode.PHARMACY_ERR_004, message);
  }
}
