package com.ph.pharmafind.application.pharmacy;

import com.ph.pharmafind.application.pharmacy.dto.PharmacyCreateRequest;
import com.ph.pharmafind.application.pharmacy.dto.PharmacyUpdateRequest;
import com.ph.pharmafind.domain.pharmacy.Owner;
import com.ph.pharmafind.domain.pharmacy.Pharmacy;
import com.ph.pharmafind.infrastructure.persistence.repository.OwnerRepository;
import com.ph.pharmafind.infrastructure.persistence.repository.PharmacyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PharmacyApplicationService {

  private final OwnerRepository ownerRepository;
  private final PharmacyRepository pharmacyRepository;

  public PharmacyApplicationService(
      OwnerRepository ownerRepository, PharmacyRepository pharmacyRepository) {
    this.ownerRepository = ownerRepository;
    this.pharmacyRepository = pharmacyRepository;
  }

  @Transactional
  public Pharmacy createPharmacy(PharmacyCreateRequest request) {
    // Vérifier l'unicité de l'email
    if (pharmacyRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("Email already exists");
    }

    // Vérifier l'unicité du téléphone
    if (pharmacyRepository.existsByPhone(request.phone())) {
      throw new IllegalArgumentException("Phone already exists");
    }

    // Créer le propriétaire
    String username = generateUsername(request.ownerFirstName(), request.ownerLastName());
    Owner owner = Owner.builder().username(username).build();
    owner = ownerRepository.save(owner);

    // Créer la pharmacie
    String gpsCoordinates = request.gpsLatitude() + "," + request.gpsLongitude();
    Pharmacy pharmacy =
        Pharmacy.builder()
            .name(request.pharmacyName())
            .email(request.email())
            .phone(request.phone())
            .city(request.city())
            .quarter(request.quarter())
            .address(request.address())
            .gpsCoordinates(gpsCoordinates)
            .owner(owner)
            .build();

    return pharmacyRepository.save(pharmacy);
  }

  @Transactional(readOnly = true)
  public Page<Pharmacy> listPharmacies(Pageable pageable, String city, String search) {
    if (city != null && !city.isEmpty()) {
      return pharmacyRepository.findByCityContainingIgnoreCase(city, pageable);
    }
    if (search != null && !search.isEmpty()) {
      return pharmacyRepository.findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(
          search, search, pageable);
    }
    return pharmacyRepository.findAll(pageable);
  }

  @Transactional(readOnly = true)
  public Pharmacy getPharmacy(UUID id) {
    return pharmacyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pharmacy not found"));
  }

  @Transactional
  public void updatePharmacy(UUID id, PharmacyUpdateRequest request) {
    Pharmacy pharmacy = getPharmacy(id);

    if (request.name() != null) {
      pharmacy.setName(request.name());
    }
    if (request.city() != null) {
      pharmacy.setCity(request.city());
    }
    if (request.quarter() != null) {
      pharmacy.setQuarter(request.quarter());
    }
    if (request.fullAddress() != null) {
      pharmacy.setAddress(request.fullAddress());
    }
    if (request.latitude() != null && request.longitude() != null) {
      pharmacy.setGpsCoordinates(request.latitude() + "," + request.longitude());
    }

    pharmacyRepository.save(pharmacy);
  }

  @Transactional
  public void deletePharmacy(UUID id) {
    Pharmacy pharmacy = getPharmacy(id);
    pharmacyRepository.delete(pharmacy);
  }

  @Transactional
  public void verifyPharmacy(UUID id, String token) {
    // TODO: Implement token verification logic
    // For now, just check if pharmacy exists
    getPharmacy(id);
    // In a real implementation, you would validate the token and update pharmacy status
  }

  private String generateUsername(String firstName, String lastName) {
    String base = (firstName + "." + lastName).toLowerCase().replaceAll("\\s+", "");
    String username = base;
    int counter = 1;
    while (ownerRepository.existsByUsername(username)) {
      username = base + counter++;
    }
    return username;
  }
}