package com.ph.backoffice.domain.certifications.port.in.impl;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.port.in.feat.ListCurrentUserPharmacies;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ListCurrentUserPharmaciesImpl implements ListCurrentUserPharmacies {

  private final PharmacyRepository pharmacyRepository;

  @Override
  public Page<Pharmacy> listCurrentUserPharmacies(UUID userId, Pageable pageable) {
    return pharmacyRepository.findByOwnerUserId(userId, pageable);
  }
}
