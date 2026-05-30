package com.ph.backoffice.domain.certifications.port.in.impl;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.exception.PharmacyNotFoundException;
import com.ph.backoffice.domain.certifications.model.PharmacyUpdateRequest;
import com.ph.backoffice.domain.certifications.port.in.feat.UpdatePharmacy;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdatePharmacyImpl implements UpdatePharmacy {

  private final PharmacyRepository pharmacyRepository;

  @Override
  public void updatePharmacy(UUID id, PharmacyUpdateRequest request) {
    Pharmacy pharmacy = pharmacyRepository
        .findById(id)
        .orElseThrow(() -> new PharmacyNotFoundException(id));

    if (request.name() != null) {
      pharmacy.setName(request.name());
    }
    if (request.city() != null) {
      pharmacy.setCity(request.city());
    }
    if (request.locality() != null) {
      pharmacy.setQuarter(request.locality());
    } else if (request.district() != null) {
      pharmacy.setQuarter(request.district());
    }
    if (request.fullAddress() != null) {
      pharmacy.setAddress(request.fullAddress());
    }
    if (request.latitude() != null && request.longitude() != null) {
      pharmacy.setGpsCoordinates(request.latitude() + "," + request.longitude());
    }

    pharmacyRepository.save(pharmacy);
  }
}
