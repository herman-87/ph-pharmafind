package com.ph.backoffice.api.certifications;

import com.ph.backoffice.application.pharmacy.PharmacyApplicationService;
import com.ph.backoffice.application.pharmacy.dto.PharmacyResponse;
import com.ph.backoffice.application.pharmacy.mapper.PharmacyMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PharmacyController implements PharmacyApi {

  private final PharmacyApplicationService pharmacyApplicationService;
  private final PharmacyMapper pharmacyMapper;

  public PharmacyController(
      PharmacyApplicationService pharmacyApplicationService, PharmacyMapper pharmacyMapper) {
    this.pharmacyApplicationService = pharmacyApplicationService;
    this.pharmacyMapper = pharmacyMapper;
  }

  @Override
  public ResponseEntity<PharmacyIdResponseDTO> createPharmacy(
      @Valid CreatePharmacyRequestDTO createPharmacyRequestDTO) {
    var request = pharmacyMapper.toCreateRequest(createPharmacyRequestDTO);
    var pharmacyId = pharmacyApplicationService.createPharmacy(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(pharmacyMapper.toPharmacyIdResponseDTO(pharmacyId));
  }

  @Override
  public ResponseEntity<PharmacyPageResponseDTO> listPharmacies(
      Integer page, Integer size, String city, String search) {
    var pageable = org.springframework.data.domain.PageRequest.of(page, size);
    Page<PharmacyResponse> pharmacies =
        pharmacyApplicationService.listPharmacies(pageable, city, search);
    return ResponseEntity.ok(pharmacyMapper.toPharmacyPageResponseDTO(pharmacies));
  }

  @Override
  public ResponseEntity<PharmacyResponseDTO> getPharmacy(UUID id) {
    PharmacyResponse pharmacy = pharmacyApplicationService.getPharmacy(id);
    return ResponseEntity.ok(pharmacyMapper.toPharmacyResponseDTO(pharmacy));
  }

  @Override
  public ResponseEntity<Void> updatePharmacy(
      UUID id, @Valid UpdatePharmacyRequestDTO updatePharmacyRequestDTO) {
    var request = pharmacyMapper.toUpdateRequest(updatePharmacyRequestDTO);
    pharmacyApplicationService.updatePharmacy(id, request);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> deletePharmacy(UUID id) {
    pharmacyApplicationService.deletePharmacy(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> verifyPharmacy(UUID id, String token) {
    pharmacyApplicationService.verifyPharmacy(id, token);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<CertificationRequestIdResponseDTO> createCertificationRequest(
      UUID id, @Valid CreateCertificationRequestDTO createCertificationRequestDTO) {
    var requestId =
        pharmacyApplicationService.createCertificationRequest(
            id,
            createCertificationRequestDTO.getDocumentUrl(),
            createCertificationRequestDTO.getNotes());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CertificationRequestIdResponseDTO().id(requestId));
  }
}
