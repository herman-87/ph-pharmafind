package com.ph.backoffice.application.pharmacy.usecase;

import com.ph.backoffice.application.pharmacy.event.CertificationRequestCreatedDomainEvent;
import com.ph.backoffice.domain.certifications.model.CertificationRequestCreateData;
import com.ph.backoffice.domain.certifications.port.in.feat.CreateCertificationRequest;
import com.ph.pharmafind.generated.event.CertificationRequestCreatedEvent;
import java.time.Clock;
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
  private final Clock clock;

  @Transactional
  public UUID execute(UUID pharmacyId, CertificationRequestCreateData data) {
    var savedCrtRequest = createCertificationRequest.createCertificationRequest(pharmacyId, data);
    var pharmacy = savedCrtRequest.getPharmacy();

    var event =
        new CertificationRequestCreatedEvent()
            .certificationRequestId(savedCrtRequest.getId())
            .pharmacyId(pharmacyId)
            .pharmacyName(pharmacy.getName())
            .ownerUserId(pharmacy.getOwner().getUserId())
            .authorizationNumber(data.authorizationNumber())
            .taxId(data.taxId())
            .legalRepresentative(data.legalRepresentative())
            .creationDate(data.creationDate().toString())
            .authorizationDocument(data.authorizationDocument())
            .businessRegistryDocument(data.businessRegistryDocument())
            .ownerIdRectoDocument(data.ownerIdRectoDocument())
            .ownerIdVersoDocument(data.ownerIdVersoDocument())
            .pharmacyLicenseDocument(data.pharmacyLicenseDocument())
            .notes(data.notes())
            .status(savedCrtRequest.getStatus().name())
            .createdAt(LocalDateTime.now(clock));

    eventPublisher.publishEvent(new CertificationRequestCreatedDomainEvent(event));

    return savedCrtRequest.getId();
  }
}
