package com.ph.backoffice.domain.certifications.exception;

public class NotPharmacyOwnerException extends DomainException {
  public NotPharmacyOwnerException() {
    super(PharmacyErrorCode.PHARMACY_ERR_006);
  }

  public NotPharmacyOwnerException(String message) {
    super(PharmacyErrorCode.PHARMACY_ERR_006, message);
  }
}
