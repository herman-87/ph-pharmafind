package com.ph.backoffice.application.pharmacy.usecase;

import cm.fastrelays.common.security.CurrentUser;
import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.port.in.feat.ListCurrentUserPharmacies;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListCurrentUserPharmaciesUseCase {

  private final ListCurrentUserPharmacies listCurrentUserPharmacies;

  @Transactional(readOnly = true)
  public Page<Pharmacy> execute(Pageable pageable) {
    var userId = CurrentUser.getUserId();
    return listCurrentUserPharmacies.listCurrentUserPharmacies(userId, pageable);
  }
}
