package com.ph.backoffice.adapters.out.persistence.jpa.repository;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.DomainPage;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
  public DomainPage<Pharmacy> findAll(int page, int size) {
    var springPage = pharmacySpringRepository.findAll(PageRequest.of(page, size));
    return toDomainPage(springPage);
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
  public DomainPage<Pharmacy> findByCityContainingIgnoreCase(String city, int page, int size) {
    var springPage =
        pharmacySpringRepository.findByCityContainingIgnoreCase(city, PageRequest.of(page, size));
    return toDomainPage(springPage);
  }

  @Override
  public DomainPage<Pharmacy> findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(
      String name, String city, int page, int size) {
    var springPage =
        pharmacySpringRepository.findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(
            name, city, PageRequest.of(page, size));
    return toDomainPage(springPage);
  }

  @Override
  public DomainPage<Pharmacy> findByOwnerUsername(String userName, int page, int size) {
    var springPage =
        pharmacySpringRepository.findAllByOwnerUsername(userName, PageRequest.of(page, size));
    return toDomainPage(springPage);
  }

  @Override
  public DomainPage<Pharmacy> findByOwnerUserId(UUID userId, int page, int size) {
    var springPage =
        pharmacySpringRepository.findByOwner_UserId(userId, PageRequest.of(page, size));
    return toDomainPage(springPage);
  }

  private DomainPage<Pharmacy> toDomainPage(org.springframework.data.domain.Page<Pharmacy> springPage) {
    return new DomainPage<>(
        springPage.getContent(),
        springPage.getNumber(),
        springPage.getSize(),
        springPage.getTotalElements(),
        springPage.getTotalPages());
  }
}
