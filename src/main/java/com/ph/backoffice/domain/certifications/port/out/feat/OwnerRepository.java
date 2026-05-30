package com.ph.backoffice.domain.certifications.port.out.feat;

import com.ph.backoffice.domain.certifications.Owner;
import java.util.Optional;
import java.util.UUID;

public interface OwnerRepository {
  Owner save(Owner owner);

  Optional<Owner> findById(UUID id);

  Optional<Owner> findByUsername(String username);

  boolean existsByUsername(String username);
}
