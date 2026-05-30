package com.ph.backoffice.domain.certifications.port.in.feat;

import com.ph.backoffice.domain.certifications.Pharmacy;
import java.util.UUID;

public interface GetPharmacy {
  Pharmacy getPharmacy(UUID id);
}
