package com.ph.backoffice.domain.certifications.port.in.feat;

import com.ph.backoffice.domain.certifications.CertificationRequest;
import java.util.UUID;

public interface GetCertificationRequest {
  CertificationRequest getCertificationRequest(UUID pharmacyId, UUID requestId, UUID callerId);
}
