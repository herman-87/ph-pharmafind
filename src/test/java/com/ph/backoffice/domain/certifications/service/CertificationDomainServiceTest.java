package com.ph.backoffice.domain.certifications.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.ph.backoffice.domain.certifications.Owner;
import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.exception.NotPharmacyOwnerException;
import com.ph.backoffice.domain.certifications.exception.PharmacyNotFoundException;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificationDomainServiceTest {

  @Mock private PharmacyRepository pharmacyRepository;
  @Mock private Pharmacy pharmacy;

  private CertificationDomainService service;
  private UUID ownerUserId;

  @BeforeEach
  void setUp() {
    service = new CertificationDomainService(pharmacyRepository);
    ownerUserId = UUID.randomUUID();
  }

  @Test
  void validateOwnership_shouldNotThrow_whenCallerIsOwner() {
    Owner owner = Owner.create("testuser", ownerUserId);
    when(pharmacy.getOwner()).thenReturn(owner);

    assertThatCode(() -> service.validateOwnership(pharmacy, ownerUserId))
        .doesNotThrowAnyException();
  }

  @Test
  void validateOwnership_shouldThrow_whenCallerIsNotOwner() {
    Owner owner = Owner.create("testuser", ownerUserId);
    when(pharmacy.getOwner()).thenReturn(owner);

    assertThatThrownBy(() -> service.validateOwnership(pharmacy, UUID.randomUUID()))
        .isInstanceOf(NotPharmacyOwnerException.class);
  }

  @Test
  void getPharmacy_shouldReturnPharmacy_whenExists() {
    UUID id = UUID.randomUUID();
    when(pharmacyRepository.findById(id)).thenReturn(Optional.of(pharmacy));

    assertThatCode(() -> service.getPharmacy(id)).doesNotThrowAnyException();
  }

  @Test
  void getPharmacy_shouldThrow_whenNotFound() {
    UUID id = UUID.randomUUID();
    when(pharmacyRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getPharmacy(id))
        .isInstanceOf(PharmacyNotFoundException.class);
  }
}
