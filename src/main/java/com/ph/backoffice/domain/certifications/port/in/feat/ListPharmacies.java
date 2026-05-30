package com.ph.backoffice.domain.certifications.port.in.feat;

import com.ph.backoffice.domain.certifications.Pharmacy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListPharmacies {
  Page<Pharmacy> listPharmacies(Pageable pageable, String city, String search);
}
