package com.ph.backoffice.domain.certifications.port.in.feat;

import java.util.UUID;

public interface DeletePharmacy {
  void deletePharmacy(UUID id, UUID callerId);
}
