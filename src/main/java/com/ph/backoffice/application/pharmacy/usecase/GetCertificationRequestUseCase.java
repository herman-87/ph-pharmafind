package com.ph.backoffice.application.pharmacy.usecase;

import com.ph.backoffice.domain.certifications.CertificationRequest;
import com.ph.backoffice.domain.certifications.port.in.feat.GetCertificationRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetCertificationRequestUseCase {

  private final GetCertificationRequest getCertificationRequest;

  @Transactional(readOnly = true)
  public CertificationRequest execute(UUID pharmacyId, UUID requestId, UUID callerId) {
    return getCertificationRequest.getCertificationRequest(pharmacyId, requestId, callerId);
  }
}
