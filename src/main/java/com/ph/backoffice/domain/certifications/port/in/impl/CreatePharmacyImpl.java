package com.ph.backoffice.domain.certifications.port.in.impl;

import com.ph.backoffice.domain.certifications.Owner;
import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.PharmacyCreateRequest;
import com.ph.backoffice.domain.certifications.port.in.feat.CreatePharmacy;
import com.ph.backoffice.domain.certifications.port.out.feat.OwnerRepository;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import com.ph.backoffice.domain.certifications.service.CertificationDomainService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreatePharmacyImpl implements CreatePharmacy {

  private final PharmacyRepository pharmacyRepository;
  private final OwnerRepository ownerRepository;
  private final CertificationDomainService pharmacyDomainService;

  @Override
  public UUID createPharmacy(PharmacyCreateRequest request, Owner owner) {
    pharmacyDomainService.validateUniqueEmail(request.email());
    pharmacyDomainService.validateUniquePhone(request.phone());

    owner = ownerRepository.save(owner);

    String gpsCoordinates = pharmacyDomainService.formatGpsCoordinates(request.latitude(), request.longitude());
    String quarter = pharmacyDomainService.resolveQuarter(request.locality(), request.district());

    Pharmacy pharmacy = Pharmacy.create(
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
}
