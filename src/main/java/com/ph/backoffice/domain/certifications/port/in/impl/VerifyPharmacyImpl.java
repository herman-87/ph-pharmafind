package com.ph.backoffice.domain.certifications.port.in.impl;

import com.ph.backoffice.domain.certifications.exception.PharmacyNotFoundException;
import com.ph.backoffice.domain.certifications.port.in.feat.VerifyPharmacy;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VerifyPharmacyImpl implements VerifyPharmacy {

  private final PharmacyRepository pharmacyRepository;

  @Override
  public void verifyPharmacy(UUID id, String token) {
    pharmacyRepository.findById(id).orElseThrow(() -> new PharmacyNotFoundException(id));
  }
}
