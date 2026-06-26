package com.ph.backoffice.domain.certifications.port.in.impl;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.exception.PharmacyNotFoundException;
import com.ph.backoffice.domain.certifications.model.PharmacyUpdateRequest;
import com.ph.backoffice.domain.certifications.port.in.feat.UpdatePharmacy;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyUpdateMapper;
import com.ph.backoffice.domain.certifications.service.CertificationDomainService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdatePharmacyImpl implements UpdatePharmacy {

  private final PharmacyRepository pharmacyRepository;
  private final PharmacyUpdateMapper pharmacyUpdateMapper;
  private final CertificationDomainService certificationDomainService;

  @Override
  public void updatePharmacy(UUID pharmacyId, PharmacyUpdateRequest request, UUID callerId) {
    Pharmacy pharmacy =
        pharmacyRepository
            .findById(pharmacyId)
            .orElseThrow(() -> new PharmacyNotFoundException(pharmacyId));
    certificationDomainService.validateOwnership(pharmacy, callerId);
    pharmacyUpdateMapper.update(request, pharmacy);

    pharmacyRepository.save(pharmacy);
  }
}
