package com.ph.backoffice.application.pharmacy.usecase;

import com.ph.backoffice.domain.certifications.port.in.feat.VerifyPharmacy;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class VerifyPharmacyUseCase {

  private final VerifyPharmacy verifyPharmacy;

  @Transactional
  public void execute(UUID id, String token) {
    verifyPharmacy.verifyPharmacy(id, token);
  }
}
