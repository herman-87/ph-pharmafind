package com.ph.backoffice.domain.certifications.port.in.impl;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.exception.PharmacyNotFoundException;
import com.ph.backoffice.domain.certifications.port.in.feat.DeletePharmacy;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import com.ph.backoffice.domain.certifications.service.CertificationDomainService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeletePharmacyImpl implements DeletePharmacy {

  private final PharmacyRepository pharmacyRepository;
  private final CertificationDomainService certificationDomainService;

  @Override
  public void deletePharmacy(UUID id, UUID callerId) {
    Pharmacy pharmacy =
        pharmacyRepository.findById(id).orElseThrow(() -> new PharmacyNotFoundException(id));

    certificationDomainService.validateOwnership(pharmacy, callerId);

    pharmacyRepository.delete(pharmacy);
  }
}
