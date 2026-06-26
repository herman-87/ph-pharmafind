package com.ph.backoffice.domain.certifications.port.in.feat;

import com.ph.backoffice.domain.certifications.CertificationRequest;
import java.util.UUID;

public interface CreateCertificationRequest {
  CertificationRequest createCertificationRequest(
      UUID pharmacyId, String documentUrl, String notes);
}
