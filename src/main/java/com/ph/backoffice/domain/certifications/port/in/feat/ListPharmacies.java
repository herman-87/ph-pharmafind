package com.ph.backoffice.domain.certifications.port.in.feat;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.DomainPage;

public interface ListPharmacies {
  DomainPage<Pharmacy> listPharmacies(int page, int size, String city, String search);
}
