package com.ph.backoffice.application.pharmacy.usecase;

import com.ph.backoffice.domain.certifications.port.in.feat.DeletePharmacy;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeletePharmacyUseCase {

  private final DeletePharmacy deletePharmacy;

  @Transactional
  public void execute(UUID id) {
    deletePharmacy.deletePharmacy(id);
  }
}
