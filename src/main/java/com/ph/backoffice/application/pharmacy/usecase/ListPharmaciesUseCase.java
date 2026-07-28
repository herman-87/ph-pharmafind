package com.ph.backoffice.application.pharmacy.usecase;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.DomainPage;
import com.ph.backoffice.domain.certifications.port.in.feat.ListPharmacies;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListPharmaciesUseCase {

  private final ListPharmacies listPharmacies;

  @Transactional(readOnly = true)
  public DomainPage<Pharmacy> execute(int page, int size, String city, String search) {
    return listPharmacies.listPharmacies(page, size, city, search);
  }
}
