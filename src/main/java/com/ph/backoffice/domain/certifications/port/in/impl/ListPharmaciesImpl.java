package com.ph.backoffice.domain.certifications.port.in.impl;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.DomainPage;
import com.ph.backoffice.domain.certifications.port.in.feat.ListPharmacies;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListPharmaciesImpl implements ListPharmacies {

  private final PharmacyRepository pharmacyRepository;

  @Override
  public DomainPage<Pharmacy> listPharmacies(int page, int size, String city, String search) {
    if (city != null && !city.isEmpty()) {
      return pharmacyRepository.findByCityContainingIgnoreCase(city, page, size);
    }
    if (search != null && !search.isEmpty()) {
      return pharmacyRepository.findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(
          search, search, page, size);
    }
    return pharmacyRepository.findAll(page, size);
  }
}
