package com.ph.backoffice.domain.certifications.port.in.feat;

import com.ph.backoffice.domain.certifications.CertificationRequest;
import com.ph.backoffice.domain.certifications.model.CertificationRequestCreateData;
import java.util.UUID;

public interface CreateCertificationRequest {
  CertificationRequest createCertificationRequest(
      UUID pharmacyId, CertificationRequestCreateData data);
}
