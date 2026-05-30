package com.ph.backoffice.domain.certifications.port.out.feat;

import com.ph.backoffice.domain.certifications.CertificationRequest;
import java.util.Optional;
import java.util.UUID;

public interface CertificationRequestRepository {
  CertificationRequest save(CertificationRequest request);

  Optional<CertificationRequest> findById(UUID id);
}
