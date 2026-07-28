package com.ph.backoffice.domain.certifications.port.out.feat;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.DomainPage;
import java.util.Optional;
import java.util.UUID;

public interface PharmacyRepository {
  Pharmacy save(Pharmacy pharmacy);

  Optional<Pharmacy> findById(UUID id);

  DomainPage<Pharmacy> findAll(int page, int size);

  void delete(Pharmacy pharmacy);

  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);

  DomainPage<Pharmacy> findByCityContainingIgnoreCase(String city, int page, int size);

  DomainPage<Pharmacy> findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(
      String name, String city, int page, int size);

  DomainPage<Pharmacy> findByOwnerUsername(String userName, int page, int size);

  DomainPage<Pharmacy> findByOwnerUserId(UUID userId, int page, int size);
}
