package com.ph.backoffice.adapters.out.persistence.jpa.repository;

import com.ph.backoffice.domain.certifications.Pharmacy;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacySpringRepository extends JpaRepository<Pharmacy, UUID> {
  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);

  Page<Pharmacy> findByCityContainingIgnoreCase(String city, Pageable pageable);

  Page<Pharmacy> findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(
      String name, String city, Pageable pageable);
}
