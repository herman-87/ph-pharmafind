package com.ph.backoffice.domain.certifications.port.in.impl;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.port.in.feat.ListMyPharmacies;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ListMyPharmaciesImpl implements ListMyPharmacies {

  private final PharmacyRepository pharmacyRepository;

  @Override
  public Page<Pharmacy> listPharmacies(Pageable pageable, String username) {
    return pharmacyRepository.findByOwnerUsername(username, pageable);
  }
}
