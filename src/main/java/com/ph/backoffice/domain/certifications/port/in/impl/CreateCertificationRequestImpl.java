package com.ph.backoffice.domain.certifications.port.in.impl;

import cm.fastrelays.common.security.CurrentUser;
import com.ph.backoffice.domain.certifications.CertificationRequest;
import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.port.in.feat.CreateCertificationRequest;
import com.ph.backoffice.domain.certifications.port.out.feat.CertificationRequestRepository;
import com.ph.backoffice.domain.certifications.service.CertificationDomainService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateCertificationRequestImpl implements CreateCertificationRequest {

  private final CertificationRequestRepository certificationRequestRepository;
  private final CertificationDomainService certificationDomainService;

  @Override
  public CertificationRequest createCertificationRequest(
      UUID pharmacyId, String documentUrl, String notes) {
    Pharmacy pharmacy = certificationDomainService.getPharmacy(pharmacyId);

    certificationDomainService.validateOwnership(pharmacy, CurrentUser.getUserId());

    CertificationRequest request = CertificationRequest.create(pharmacy, documentUrl, notes);
    return certificationRequestRepository.save(request);
  }
}
