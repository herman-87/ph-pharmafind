package com.ph.backoffice.application.pharmacy;

import cm.fastrelays.common.exception.BadRequestException;
import cm.fastrelays.common.exception.ConflictException;
import cm.fastrelays.common.exception.ResourceNotFoundException;
import cm.fastrelays.common.security.CurrentUser;
import com.ph.backoffice.ErrorCode;
import com.ph.backoffice.application.pharmacy.dto.PharmacyCreateRequest;
import com.ph.backoffice.application.pharmacy.dto.PharmacyResponse;
import com.ph.backoffice.application.pharmacy.dto.PharmacyUpdateRequest;
import com.ph.backoffice.application.pharmacy.mapper.PharmacyMapper;
import com.ph.backoffice.domain.certifications.CertificationRequest;
import com.ph.backoffice.domain.certifications.Owner;
import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.infrastructure.persistence.repository.CertificationRequestRepository;
import com.ph.backoffice.infrastructure.persistence.repository.OwnerRepository;
import com.ph.backoffice.infrastructure.persistence.repository.PharmacyRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PharmacyApplicationService {

  private final OwnerRepository ownerRepository;
  private final PharmacyRepository pharmacyRepository;
  private final CertificationRequestRepository certificationRequestRepository;
  private final PharmacyMapper pharmacyMapper;

  public PharmacyApplicationService(
      OwnerRepository ownerRepository,
      PharmacyRepository pharmacyRepository,
      CertificationRequestRepository certificationRequestRepository,
      PharmacyMapper pharmacyMapper) {
    this.ownerRepository = ownerRepository;
    this.pharmacyRepository = pharmacyRepository;
    this.certificationRequestRepository = certificationRequestRepository;
    this.pharmacyMapper = pharmacyMapper;
  }

  @Transactional
  public UUID createPharmacy(PharmacyCreateRequest request) {
    if (!request.password().equals(request.confirmPassword())) {
      throw new BadRequestException(
          ErrorCode.PASSWORD_MISMATCH, "Password confirmation does not match");
    }

    if (pharmacyRepository.existsByEmail(request.email())) {
      throw new ConflictException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email already exists");
    }

    if (pharmacyRepository.existsByPhone(request.phone())) {
      throw new ConflictException(ErrorCode.PHONE_ALREADY_EXISTS, "Phone already exists");
    }

    Owner owner = Owner.create(CurrentUser.getUserName(), CurrentUser.getUserId());
    owner = ownerRepository.save(owner);

    String gpsCoordinates = request.latitude() + "," + request.longitude();
    String quarter =
        request.locality() != null && !request.locality().isBlank()
            ? request.locality()
            : request.district();

    Pharmacy pharmacy =
        Pharmacy.create(
            request.name(),
            request.email(),
            request.phone(),
            request.city(),
            quarter,
            request.fullAddress(),
            gpsCoordinates,
            owner);

    return pharmacyRepository.save(pharmacy).getId();
  }

  @Transactional(readOnly = true)
  public Page<PharmacyResponse> listPharmacies(Pageable pageable, String city, String search) {
    if (city != null && !city.isEmpty()) {
      return pharmacyRepository
          .findByCityContainingIgnoreCase(city, pageable)
          .map(pharmacyMapper::toResponse);
    }
    if (search != null && !search.isEmpty()) {
      return pharmacyRepository
          .findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(search, search, pageable)
          .map(pharmacyMapper::toResponse);
    }
    return pharmacyRepository.findAll(pageable).map(pharmacyMapper::toResponse);
  }

  @Transactional(readOnly = true)
  public PharmacyResponse getPharmacy(UUID id) {
    return pharmacyRepository
        .findById(id)
        .map(pharmacyMapper::toResponse)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(ErrorCode.PHARMACY_NOT_FOUND, "Pharmacy not found"));
  }

  @Transactional
  public void updatePharmacy(UUID id, PharmacyUpdateRequest request) {
    Pharmacy pharmacy = getPharmacyById(id);

    if (request.name() != null) {
      pharmacy.setName(request.name());
    }
    if (request.city() != null) {
      pharmacy.setCity(request.city());
    }
    if (request.locality() != null) {
      pharmacy.setQuarter(request.locality());
    } else if (request.district() != null) {
      pharmacy.setQuarter(request.district());
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
    Pharmacy pharmacy = getPharmacyById(id);
    pharmacyRepository.delete(pharmacy);
  }

  @Transactional
  public void verifyPharmacy(UUID id, String token) {
    getPharmacyById(id);
  }

  @Transactional
  public UUID createCertificationRequest(UUID pharmacyId, String documentUrl, String notes) {
    Pharmacy pharmacy = getPharmacyById(pharmacyId);

    if (!pharmacy.getOwner().getUserId().equals(CurrentUser.getUserId())) {
      throw new cm.fastrelays.common.exception.ForbiddenException(
          "Only the owner can request certification");
    }

    CertificationRequest request = CertificationRequest.create(pharmacy, documentUrl, notes);
    return certificationRequestRepository.save(request).getId();
  }

  private Pharmacy getPharmacyById(UUID id) {
    return pharmacyRepository
        .findById(id)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(ErrorCode.PHARMACY_NOT_FOUND, "Pharmacy not found"));
  }
}
