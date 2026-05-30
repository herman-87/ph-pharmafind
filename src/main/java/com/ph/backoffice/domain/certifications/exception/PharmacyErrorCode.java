package com.ph.backoffice.domain.certifications.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PharmacyErrorCode {
  PHARMACY_ERR_001("ERR-PHARMACY-001", "Pharmacy already exists"),
  PHARMACY_ERR_002("ERR-PHARMACY-002", "Pharmacy not found"),
  PHARMACY_ERR_003("ERR-PHARMACY-003", "Email already exists"),
  PHARMACY_ERR_004("ERR-PHARMACY-004", "Phone already exists"),
  PHARMACY_ERR_005("ERR-PHARMACY-005", "Access denied"),
  PHARMACY_ERR_006("ERR-PHARMACY-006", "Not the pharmacy owner");

  private final String code;
  private final String message;
}
