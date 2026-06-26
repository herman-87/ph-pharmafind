package com.ph.backoffice.domain.certifications.port.out.feat;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.PharmacyUpdateRequest;

public interface PharmacyUpdateMapper {
  void update(PharmacyUpdateRequest request, Pharmacy pharmacy);
}
