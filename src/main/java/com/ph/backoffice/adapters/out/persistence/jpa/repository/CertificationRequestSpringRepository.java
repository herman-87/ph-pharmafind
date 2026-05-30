package com.ph.backoffice.adapters.out.persistence.jpa.repository;

import com.ph.backoffice.domain.certifications.CertificationRequest;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRequestSpringRepository extends JpaRepository<CertificationRequest, UUID> {}
