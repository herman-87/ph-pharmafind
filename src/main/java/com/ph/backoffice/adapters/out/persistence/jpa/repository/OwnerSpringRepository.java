package com.ph.backoffice.adapters.out.persistence.jpa.repository;

import com.ph.backoffice.domain.certifications.Owner;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerSpringRepository extends JpaRepository<Owner, UUID> {
  Optional<Owner> findByUsername(String username);

  boolean existsByUsername(String username);
}
