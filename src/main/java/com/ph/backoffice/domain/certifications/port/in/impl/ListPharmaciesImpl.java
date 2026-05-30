package com.ph.backoffice.domain.certifications.port.in.impl;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.port.in.feat.ListPharmacies;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ListPharmaciesImpl implements ListPharmacies {

  private final PharmacyRepository pharmacyRepository;

  @Override
  public Page<Pharmacy> listPharmacies(Pageable pageable, String city, String search) {
    if (city != null && !city.isEmpty()) {
      return pharmacyRepository.findByCityContainingIgnoreCase(city, pageable);
    }
    if (search != null && !search.isEmpty()) {
      return pharmacyRepository.findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(
          search, search, pageable);
    }
    return pharmacyRepository.findAll(pageable);
  }
}
