package com.ph.backoffice.domain.certifications.port.in.feat;

import java.util.UUID;

public interface VerifyPharmacy {
  void verifyPharmacy(UUID id, String token);
}
