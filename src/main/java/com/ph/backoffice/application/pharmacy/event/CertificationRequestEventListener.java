package com.ph.backoffice.application.pharmacy.event;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CertificationRequestEventListener {

  private static final Logger log =
      LoggerFactory.getLogger(CertificationRequestEventListener.class);

  private final CertificationRequestEventPublisher eventPublisher;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onCertificationRequestCreated(CertificationRequestCreatedDomainEvent event) {
    log.info(
        "Transaction committed — publishing CertificationRequestCreatedEvent: requestId={}, pharmacyId={}",
        event.event().getCertificationRequestId(),
        event.event().getPharmacyId());
    eventPublisher.certificationRequestCreated(event.event());
  }
}
