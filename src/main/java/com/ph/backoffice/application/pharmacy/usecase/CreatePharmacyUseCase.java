package com.ph.backoffice.application.pharmacy.usecase;

import cm.fastrelays.common.security.CurrentUser;
import com.ph.backoffice.domain.certifications.Owner;
import com.ph.backoffice.domain.certifications.model.PharmacyCreateRequest;
import com.ph.backoffice.domain.certifications.port.in.feat.CreatePharmacy;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreatePharmacyUseCase {

  private final CreatePharmacy createPharmacy;

  @Transactional
  public UUID execute(PharmacyCreateRequest request) {
    Owner owner = Owner.create(CurrentUser.getUserName(), CurrentUser.getUserId());
    return createPharmacy.createPharmacy(request, owner);
  }
}
