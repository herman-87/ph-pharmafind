package com.ph.backoffice.application.pharmacy.usecase;

import com.ph.backoffice.application.pharmacy.event.CertificationRequestCreatedDomainEvent;
import com.ph.backoffice.domain.certifications.port.in.feat.CreateCertificationRequest;
import com.ph.pharmafind.generated.event.CertificationRequestCreatedEvent;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateCertificationRequestUseCase {

  private final CreateCertificationRequest createCertificationRequest;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public UUID execute(UUID pharmacyId, String documentUrl, String notes) {
    var saved =
        createCertificationRequest.createCertificationRequest(pharmacyId, documentUrl, notes);
    var pharmacy = saved.getPharmacy();

    var event =
        new CertificationRequestCreatedEvent()
            .certificationRequestId(saved.getId())
            .pharmacyId(pharmacyId)
            .pharmacyName(pharmacy.getName())
            .ownerUserId(pharmacy.getOwner().getUserId())
            .documentUrl(documentUrl)
            .notes(notes)
            .status(saved.getStatus().name())
            .createdAt(LocalDateTime.now());

    eventPublisher.publishEvent(new CertificationRequestCreatedDomainEvent(event));

    return saved.getId();
  }
}
