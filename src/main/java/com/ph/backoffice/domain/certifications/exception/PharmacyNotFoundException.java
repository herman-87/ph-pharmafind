package com.ph.backoffice.domain.certifications.exception;

import java.util.UUID;

public class PharmacyNotFoundException extends DomainException {
  public PharmacyNotFoundException(UUID id) {
    super(PharmacyErrorCode.PHARMACY_ERR_002, "Pharmacy not found with id: " + id);
  }
}
