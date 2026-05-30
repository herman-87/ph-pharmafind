package com.ph.backoffice.domain.certifications.port.in.feat;

import com.ph.backoffice.domain.certifications.Owner;
import com.ph.backoffice.domain.certifications.model.PharmacyCreateRequest;
import java.util.UUID;

public interface CreatePharmacy {
  UUID createPharmacy(PharmacyCreateRequest request, Owner owner);
}
