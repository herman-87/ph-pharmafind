package com.ph.backoffice.domain.certifications.port.in.feat;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.DomainPage;
import java.util.UUID;

public interface ListCurrentUserPharmacies {
  DomainPage<Pharmacy> listCurrentUserPharmacies(UUID userId, int page, int size);
}
