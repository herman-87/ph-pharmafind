package com.ph.backoffice.application.pharmacy.usecase;

import cm.fastrelays.common.security.CurrentUser;
import com.ph.backoffice.domain.certifications.model.PharmacyUpdateRequest;
import com.ph.backoffice.domain.certifications.port.in.feat.UpdatePharmacy;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdatePharmacyUseCase {

  private final UpdatePharmacy updatePharmacy;

  @Transactional
  public void execute(UUID id, PharmacyUpdateRequest request) {
    updatePharmacy.updatePharmacy(id, request, CurrentUser.getUserId());
  }
}
