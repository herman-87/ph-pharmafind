package com.ph.backoffice.domain.certifications.port.in.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.exception.NotPharmacyOwnerException;
import com.ph.backoffice.domain.certifications.exception.PharmacyNotFoundException;
import com.ph.backoffice.domain.certifications.model.PharmacyUpdateRequest;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyUpdateMapper;
import com.ph.backoffice.domain.certifications.service.CertificationDomainService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdatePharmacyImplTest {

  @Mock private PharmacyRepository pharmacyRepository;
  @Mock private PharmacyUpdateMapper pharmacyUpdateMapper;
  @Mock private CertificationDomainService certificationDomainService;

  private UpdatePharmacyImpl updatePharmacyImpl;

  private UUID pharmacyId;
  private UUID callerId;
  private Pharmacy pharmacy;
  private PharmacyUpdateRequest request;

  @BeforeEach
  void setUp() {
    updatePharmacyImpl =
        new UpdatePharmacyImpl(
            pharmacyRepository, pharmacyUpdateMapper, certificationDomainService);

    pharmacyId = UUID.randomUUID();
    callerId = UUID.randomUUID();
    pharmacy = mock(Pharmacy.class);
    request = mock(PharmacyUpdateRequest.class);
  }

  @Test
  void shouldUpdatePharmacy_whenOwner() {
    when(pharmacyRepository.findById(pharmacyId)).thenReturn(Optional.of(pharmacy));
    doNothing().when(certificationDomainService).validateOwnership(pharmacy, callerId);
    when(pharmacyRepository.save(pharmacy)).thenReturn(pharmacy);

    updatePharmacyImpl.updatePharmacy(pharmacyId, request, callerId);

    verify(pharmacyRepository).findById(pharmacyId);
    verify(certificationDomainService).validateOwnership(pharmacy, callerId);
    verify(pharmacyUpdateMapper).update(request, pharmacy);
    verify(pharmacyRepository).save(pharmacy);
  }

  @Test
  void shouldThrowPharmacyNotFoundException_whenNotFound() {
    when(pharmacyRepository.findById(pharmacyId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> updatePharmacyImpl.updatePharmacy(pharmacyId, request, callerId))
        .isInstanceOf(PharmacyNotFoundException.class);

    verify(pharmacyRepository).findById(pharmacyId);
    verifyNoInteractions(certificationDomainService, pharmacyUpdateMapper);
    verify(pharmacyRepository, never()).save(any());
  }

  @Test
  void shouldThrowNotPharmacyOwnerException_whenNotOwner() {
    when(pharmacyRepository.findById(pharmacyId)).thenReturn(Optional.of(pharmacy));
    doThrow(new NotPharmacyOwnerException())
        .when(certificationDomainService)
        .validateOwnership(pharmacy, callerId);

    assertThatThrownBy(() -> updatePharmacyImpl.updatePharmacy(pharmacyId, request, callerId))
        .isInstanceOf(NotPharmacyOwnerException.class);

    verify(pharmacyRepository).findById(pharmacyId);
    verify(certificationDomainService).validateOwnership(pharmacy, callerId);
    verifyNoInteractions(pharmacyUpdateMapper);
    verify(pharmacyRepository, never()).save(any());
  }
}
