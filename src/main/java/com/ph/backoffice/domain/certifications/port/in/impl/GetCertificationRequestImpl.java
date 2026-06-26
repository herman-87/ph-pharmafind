package com.ph.backoffice.domain.certifications.port.in.impl;

import cm.fastrelays.common.security.CurrentUser;
import com.ph.backoffice.domain.certifications.CertificationRequest;
import com.ph.backoffice.domain.certifications.exception.CertificationRequestNotFoundException;
import com.ph.backoffice.domain.certifications.port.in.feat.GetCertificationRequest;
import com.ph.backoffice.domain.certifications.port.out.feat.CertificationRequestRepository;
import com.ph.backoffice.domain.certifications.service.CertificationDomainService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetCertificationRequestImpl implements GetCertificationRequest {

  private final CertificationRequestRepository certificationRequestRepository;
  private final CertificationDomainService certificationDomainService;

  @Override
  public CertificationRequest getCertificationRequest(UUID pharmacyId, UUID requestId) {
    var pharmacy = certificationDomainService.getPharmacy(pharmacyId);
    certificationDomainService.validateOwnership(pharmacy, CurrentUser.getUserId());

    return certificationRequestRepository
        .findById(requestId)
        .filter(req -> req.getPharmacy().getId().equals(pharmacyId))
        .orElseThrow(() -> new CertificationRequestNotFoundException(requestId));
  }
}
