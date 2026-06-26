package com.ph.backoffice.domain.certifications.port.in.feat;

import com.ph.backoffice.domain.certifications.Pharmacy;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListCurrentUserPharmacies {
  Page<Pharmacy> listCurrentUserPharmacies(UUID userId, Pageable pageable);
}
