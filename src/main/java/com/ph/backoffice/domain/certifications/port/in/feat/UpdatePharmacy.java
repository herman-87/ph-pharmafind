package com.ph.backoffice.domain.certifications.port.in.feat;

import com.ph.backoffice.domain.certifications.model.PharmacyUpdateRequest;
import java.util.UUID;

public interface UpdatePharmacy {
  void updatePharmacy(UUID id, PharmacyUpdateRequest request);
}
