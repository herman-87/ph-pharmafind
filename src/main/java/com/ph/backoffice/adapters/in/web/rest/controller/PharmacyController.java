package com.ph.backoffice.adapters.in.web.rest.controller;

import cm.fastrelays.common.security.CurrentUser;
import com.ph.backoffice.adapters.in.web.rest.mapper.PharmacyMapper;
import com.ph.backoffice.application.pharmacy.usecase.CreateCertificationRequestUseCase;
import com.ph.backoffice.application.pharmacy.usecase.CreatePharmacyUseCase;
import com.ph.backoffice.application.pharmacy.usecase.DeletePharmacyUseCase;
import com.ph.backoffice.application.pharmacy.usecase.GetCertificationRequestUseCase;
import com.ph.backoffice.application.pharmacy.usecase.GetPharmacyUseCase;
import com.ph.backoffice.application.pharmacy.usecase.ListCurrentUserPharmaciesUseCase;
import com.ph.backoffice.application.pharmacy.usecase.ListPharmaciesUseCase;
import com.ph.backoffice.application.pharmacy.usecase.UpdatePharmacyUseCase;
import com.ph.backoffice.application.pharmacy.usecase.VerifyPharmacyUseCase;
import com.ph.backoffice.domain.certifications.model.CertificationRequestCreateData;
import com.ph.pharmafind.generated.api.PharmacyApi;
import com.ph.pharmafind.generated.model.CertificationRequestIdResponseDTO;
import com.ph.pharmafind.generated.model.CertificationRequestResponseDTO;
import com.ph.pharmafind.generated.model.CreateCertificationRequestDTO;
import com.ph.pharmafind.generated.model.CreatePharmacyRequestDTO;
import com.ph.pharmafind.generated.model.PharmacyIdResponseDTO;
import com.ph.pharmafind.generated.model.PharmacyPageResponseDTO;
import com.ph.pharmafind.generated.model.PharmacyResponseDTO;
import com.ph.pharmafind.generated.model.UpdatePharmacyRequestDTO;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PharmacyController implements PharmacyApi {

  private final CreatePharmacyUseCase createPharmacyUseCase;
  private final ListPharmaciesUseCase listPharmaciesUseCase;
  private final GetPharmacyUseCase getPharmacyUseCase;
  private final UpdatePharmacyUseCase updatePharmacyUseCase;
  private final DeletePharmacyUseCase deletePharmacyUseCase;
  private final VerifyPharmacyUseCase verifyPharmacyUseCase;
  private final CreateCertificationRequestUseCase createCertificationRequestUseCase;
  private final GetCertificationRequestUseCase getCertificationRequestUseCase;
  private final ListCurrentUserPharmaciesUseCase listCurrentUserPharmaciesUseCase;
  private final PharmacyMapper pharmacyMapper;

  @Override
  public ResponseEntity<PharmacyIdResponseDTO> createPharmacy(
      @Valid CreatePharmacyRequestDTO createPharmacyRequestDTO) {
    var request = pharmacyMapper.toCreateRequest(createPharmacyRequestDTO);
    var pharmacyId = createPharmacyUseCase.execute(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(pharmacyMapper.toPharmacyIdResponseDTO(pharmacyId));
  }

  @Override
  public ResponseEntity<PharmacyPageResponseDTO> listPharmacies(
      Integer page, Integer size, String city, String search) {
    var pharmacies = listPharmaciesUseCase.execute(page, size, city, search);
    return ResponseEntity.ok(pharmacyMapper.toPharmacyPageResponseDTO(pharmacies));
  }

  @Override
  public ResponseEntity<PharmacyResponseDTO> getPharmacy(UUID id) {
    var pharmacy = getPharmacyUseCase.execute(id);
    return ResponseEntity.ok(pharmacyMapper.toPharmacyResponseDTO(pharmacy));
  }

  @Override
  public ResponseEntity<Void> updatePharmacy(
      UUID id, @Valid UpdatePharmacyRequestDTO updatePharmacyRequestDTO) {
    var request = pharmacyMapper.toUpdateRequest(updatePharmacyRequestDTO);
    updatePharmacyUseCase.execute(id, request);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> deletePharmacy(UUID id) {
    deletePharmacyUseCase.execute(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> verifyPharmacy(UUID id, String token) {
    verifyPharmacyUseCase.execute(id, token);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<PharmacyPageResponseDTO> getMyPharmacies(Integer page, Integer size) {
    var pharmacies = listCurrentUserPharmaciesUseCase.execute(page, size);
    return ResponseEntity.ok(pharmacyMapper.toPharmacyPageResponseDTO(pharmacies));
  }

  @Override
  public ResponseEntity<CertificationRequestIdResponseDTO> createCertificationRequest(
      UUID id, @Valid CreateCertificationRequestDTO dto) {
    var data =
        new CertificationRequestCreateData(
            dto.getAuthorizationNumber(),
            dto.getTaxId(),
            dto.getLegalRepresentative(),
            dto.getCreationDate(),
            dto.getAuthorizationDocument(),
            dto.getBusinessRegistryDocument(),
            dto.getOwnerIdRectoDocument(),
            dto.getOwnerIdVersoDocument(),
            dto.getPharmacyLicenseDocument(),
            dto.getNotes());
    var callerId = CurrentUser.getUserId();
    var requestId = createCertificationRequestUseCase.execute(id, data, callerId);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CertificationRequestIdResponseDTO().id(requestId));
  }

  @Override
  public ResponseEntity<CertificationRequestResponseDTO> getCertificationRequest(
      UUID pharmacyId, UUID requestId) {
    var callerId = CurrentUser.getUserId();
    var request = getCertificationRequestUseCase.execute(pharmacyId, requestId, callerId);
    return ResponseEntity.ok(toResponseDTO(request));
  }

  private static CertificationRequestResponseDTO toResponseDTO(
      com.ph.backoffice.domain.certifications.CertificationRequest request) {
    return new CertificationRequestResponseDTO()
        .id(request.getId())
        .pharmacyId(request.getPharmacy().getId())
        .authorizationNumber(request.getAuthorizationNumber())
        .taxId(request.getTaxId())
        .legalRepresentative(request.getLegalRepresentative())
        .creationDate(request.getCreationDate())
        .authorizationDocument(request.getAuthorizationDocument())
        .businessRegistryDocument(request.getBusinessRegistryDocument())
        .ownerIdRectoDocument(request.getOwnerIdRectoDocument())
        .ownerIdVersoDocument(request.getOwnerIdVersoDocument())
        .pharmacyLicenseDocument(request.getPharmacyLicenseDocument())
        .notes(request.getNotes())
        .status(request.getStatus().name())
        .createdAt(request.getCreatedAt())
        .updatedAt(request.getUpdatedAt());
  }
}
