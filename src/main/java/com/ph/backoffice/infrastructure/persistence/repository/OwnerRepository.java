package com.ph.backoffice.infrastructure.persistence.repository;

import com.ph.backoffice.domain.certifications.Owner;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, UUID> {
  Optional<Owner> findByUsername(String username);

  boolean existsByUsername(String username);
}
