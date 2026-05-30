package com.ph.backoffice.domain.certifications.port.out.feat;

import com.ph.backoffice.domain.certifications.Pharmacy;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PharmacyRepository {
  Pharmacy save(Pharmacy pharmacy);

  Optional<Pharmacy> findById(UUID id);

  Page<Pharmacy> findAll(Pageable pageable);

  void delete(Pharmacy pharmacy);

  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);

  Page<Pharmacy> findByCityContainingIgnoreCase(String city, Pageable pageable);

  Page<Pharmacy> findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(
      String name, String city, Pageable pageable);
}
