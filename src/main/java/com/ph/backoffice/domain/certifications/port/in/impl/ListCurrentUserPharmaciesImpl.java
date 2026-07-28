package com.ph.backoffice.domain.certifications.port.in.impl;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.DomainPage;
import com.ph.backoffice.domain.certifications.port.in.feat.ListCurrentUserPharmacies;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListCurrentUserPharmaciesImpl implements ListCurrentUserPharmacies {

  private final PharmacyRepository pharmacyRepository;

  @Override
  public DomainPage<Pharmacy> listCurrentUserPharmacies(UUID userId, int page, int size) {
    return pharmacyRepository.findByOwnerUserId(userId, page, size);
  }
}
