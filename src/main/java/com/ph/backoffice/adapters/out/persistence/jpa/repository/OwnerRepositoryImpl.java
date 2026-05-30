package com.ph.backoffice.adapters.out.persistence.jpa.repository;

import com.ph.backoffice.domain.certifications.Owner;
import com.ph.backoffice.domain.certifications.port.out.feat.OwnerRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerRepositoryImpl implements OwnerRepository {

  private final OwnerSpringRepository ownerSpringRepository;

  @Override
  public Owner save(Owner owner) {
    return ownerSpringRepository.save(owner);
  }

  @Override
  public Optional<Owner> findById(UUID id) {
    return ownerSpringRepository.findById(id);
  }

  @Override
  public Optional<Owner> findByUsername(String username) {
    return ownerSpringRepository.findByUsername(username);
  }

  @Override
  public boolean existsByUsername(String username) {
    return ownerSpringRepository.existsByUsername(username);
  }
}
