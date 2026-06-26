package com.ph.backoffice.domain.certifications.service;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.exception.EmailAlreadyExistsException;
import com.ph.backoffice.domain.certifications.exception.NotPharmacyOwnerException;
import com.ph.backoffice.domain.certifications.exception.PharmacyNotFoundException;
import com.ph.backoffice.domain.certifications.exception.PhoneAlreadyExistsException;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

@RequiredArgsConstructor
public class CertificationDomainService {

  private final PharmacyRepository pharmacyRepository;

  public void validateUniqueEmail(String email) {
    if (email != null && pharmacyRepository.existsByEmail(email)) {
      throw new EmailAlreadyExistsException();
    }
  }

  public void validateUniquePhone(String phone) {
    if (phone != null && pharmacyRepository.existsByPhone(phone)) {
      throw new PhoneAlreadyExistsException();
    }
  }

  public String formatGpsCoordinates(Double latitude, Double longitude) {
    return latitude + "," + longitude;
  }

  public String resolveQuarter(String locality, String district) {
    return locality != null && !locality.isBlank() ? locality : district;
  }

  public @NonNull Pharmacy getPharmacy(UUID pharmacyId) {
    return pharmacyRepository
        .findById(pharmacyId)
        .orElseThrow(() -> new PharmacyNotFoundException(pharmacyId));
  }

  public void validateOwnership(Pharmacy pharmacy, UUID userId) {
    if (!pharmacy.getOwner().getUserId().equals(userId)) {
      throw new NotPharmacyOwnerException();
    }
  }
}
