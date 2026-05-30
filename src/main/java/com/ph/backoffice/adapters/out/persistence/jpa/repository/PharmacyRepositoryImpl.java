package com.ph.backoffice.adapters.out.persistence.jpa.repository;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PharmacyRepositoryImpl implements PharmacyRepository {

  private final PharmacySpringRepository pharmacySpringRepository;

  @Override
  public Pharmacy save(Pharmacy pharmacy) {
    return pharmacySpringRepository.save(pharmacy);
  }

  @Override
  public Optional<Pharmacy> findById(UUID id) {
    return pharmacySpringRepository.findById(id);
  }

  @Override
  public Page<Pharmacy> findAll(Pageable pageable) {
    return pharmacySpringRepository.findAll(pageable);
  }

  @Override
  public void delete(Pharmacy pharmacy) {
    pharmacySpringRepository.delete(pharmacy);
  }

  @Override
  public boolean existsByEmail(String email) {
    return pharmacySpringRepository.existsByEmail(email);
  }

  @Override
  public boolean existsByPhone(String phone) {
    return pharmacySpringRepository.existsByPhone(phone);
  }

  @Override
  public Page<Pharmacy> findByCityContainingIgnoreCase(String city, Pageable pageable) {
    return pharmacySpringRepository.findByCityContainingIgnoreCase(city, pageable);
  }

  @Override
  public Page<Pharmacy> findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(
      String name, String city, Pageable pageable) {
    return pharmacySpringRepository.findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(
        name, city, pageable);
  }
}
