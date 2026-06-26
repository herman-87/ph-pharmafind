package com.ph.backoffice.domain.certifications.port.in.impl;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.exception.PharmacyNotFoundException;
import com.ph.backoffice.domain.certifications.port.in.feat.GetPharmacy;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetPharmacyImpl implements GetPharmacy {

  private final PharmacyRepository pharmacyRepository;

  @Override
  public Pharmacy getPharmacy(UUID id) {
    return pharmacyRepository.findById(id).orElseThrow(() -> new PharmacyNotFoundException(id));
  }
}
