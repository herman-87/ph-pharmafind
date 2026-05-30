package com.ph.backoffice.adapters.in.web.rest.controller;

import com.ph.backoffice.adapters.in.web.rest.mapper.PharmacyMapper;
import com.ph.backoffice.application.pharmacy.usecase.CreateCertificationRequestUseCase;
import com.ph.backoffice.application.pharmacy.usecase.CreatePharmacyUseCase;
import com.ph.backoffice.application.pharmacy.usecase.DeletePharmacyUseCase;
import com.ph.backoffice.application.pharmacy.usecase.GetPharmacyUseCase;
import com.ph.backoffice.application.pharmacy.usecase.ListPharmaciesUseCase;
import com.ph.backoffice.application.pharmacy.usecase.UpdatePharmacyUseCase;
import com.ph.backoffice.application.pharmacy.usecase.VerifyPharmacyUseCase;
import com.ph.pharmafind.generated.api.PharmacyApi;
import com.ph.pharmafind.generated.model.CertificationRequestIdResponseDTO;
import com.ph.pharmafind.generated.model.CreateCertificationRequestDTO;
import com.ph.pharmafind.generated.model.CreatePharmacyRequestDTO;
import com.ph.pharmafind.generated.model.PharmacyIdResponseDTO;
import com.ph.pharmafind.generated.model.PharmacyPageResponseDTO;
import com.ph.pharmafind.generated.model.PharmacyResponseDTO;
import com.ph.pharmafind.generated.model.UpdatePharmacyRequestDTO;
import jakarta.validation.Valid;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    var pageable = PageRequest.of(page, size);
    var pharmacies = listPharmaciesUseCase.execute(pageable, city, search);
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
  public ResponseEntity<CertificationRequestIdResponseDTO> createCertificationRequest(
      UUID id, @Valid CreateCertificationRequestDTO createCertificationRequestDTO) {
    var requestId = createCertificationRequestUseCase.execute(
        id,
        createCertificationRequestDTO.getDocumentUrl(),
        createCertificationRequestDTO.getNotes());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CertificationRequestIdResponseDTO().id(requestId));
  }
}
