package com.ph.backoffice.adapters.out.persistence.jpa.repository;

import com.ph.backoffice.domain.certifications.CertificationRequest;
import com.ph.backoffice.domain.certifications.port.out.feat.CertificationRequestRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CertificationRequestRepositoryImpl implements CertificationRequestRepository {

  private final CertificationRequestSpringRepository certificationRequestSpringRepository;

  @Override
  public CertificationRequest save(CertificationRequest request) {
    return certificationRequestSpringRepository.save(request);
  }

  @Override
  public Optional<CertificationRequest> findById(UUID id) {
    return certificationRequestSpringRepository.findById(id);
  }
}
