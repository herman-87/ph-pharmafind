package com.ph.backoffice.application.pharmacy.event;

import com.ph.pharmafind.generated.event.CertificationRequestCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CertificationRequestEventPublisher {

  private static final String CERTIFICATION_REQUEST_CREATED_CHANNEL = "certificationRequestCreated-out-0";

  private final StreamBridge streamBridge;

  public void certificationRequestCreated(CertificationRequestCreatedEvent event) {
    log.info("Publishing CertificationRequestCreatedEvent: requestId={}, pharmacyId={}",
            event.getCertificationRequestId(), event.getPharmacyId());
    streamBridge.send(CERTIFICATION_REQUEST_CREATED_CHANNEL, event);
  }
}
