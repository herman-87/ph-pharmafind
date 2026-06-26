package com.ph.backoffice.domain.certifications.port.in.impl;

import com.ph.backoffice.domain.certifications.Owner;
import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.PharmacyCreateRequest;
import com.ph.backoffice.domain.certifications.model.PharmacyData;
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

    PharmacyData data =
        new PharmacyData(
            request.name(),
            request.email(),
            request.phone(),
            request.city(),
            pharmacyDomainService.resolveQuarter(request.locality(), request.district()),
            request.fullAddress(),
            pharmacyDomainService.formatGpsCoordinates(request.latitude(), request.longitude()),
            request.registrationNumber(),
            request.taxId(),
            request.is24h(),
            request.deliveryAvailable(),
            request.deliveryRadiusKm(),
            request.openingHours(),
            request.paymentMethods(),
            request.socialLinks());

    return pharmacyRepository.save(Pharmacy.create(data, owner)).getId();
  }
}
