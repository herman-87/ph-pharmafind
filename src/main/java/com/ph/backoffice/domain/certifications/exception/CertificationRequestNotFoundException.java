package com.ph.backoffice.domain.certifications.exception;

import java.util.UUID;

public class CertificationRequestNotFoundException extends DomainException {

  public CertificationRequestNotFoundException(UUID id) {
    super(PharmacyErrorCode.PHARMACY_ERR_007, "Certification request not found with id: " + id);
  }
}
