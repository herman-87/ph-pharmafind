package com.ph.backoffice.application.pharmacy.usecase;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.port.in.feat.GetPharmacy;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetPharmacyUseCase {

  private final GetPharmacy getPharmacy;

  @Transactional(readOnly = true)
  public Pharmacy execute(UUID id) {
    return getPharmacy.getPharmacy(id);
  }
}
