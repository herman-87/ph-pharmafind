package com.ph.pharmafind.api.pharmacy;

import com.ph.pharmafind.application.pharmacy.PharmacyApplicationService;
import com.ph.pharmafind.application.pharmacy.dto.PharmacyCreateRequest;
import com.ph.pharmafind.application.pharmacy.dto.PharmacyResponse;
import com.ph.pharmafind.application.pharmacy.dto.PharmacyUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyController {

  private final PharmacyApplicationService pharmacyApplicationService;

  public PharmacyController(PharmacyApplicationService pharmacyApplicationService) {
    this.pharmacyApplicationService = pharmacyApplicationService;
  }

  @PostMapping
  public ResponseEntity<PharmacyResponse> createPharmacy(
      @Valid @RequestBody PharmacyCreateRequest request) {
    PharmacyResponse pharmacy = pharmacyApplicationService.createPharmacy(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(pharmacy);
  }

  @GetMapping
  public ResponseEntity<Page<PharmacyResponse>> listPharmacies(
      @PageableDefault(size = 20) Pageable pageable,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String search) {
    Page<PharmacyResponse> pharmacies =
        pharmacyApplicationService.listPharmacies(pageable, city, search);
    return ResponseEntity.ok(pharmacies);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PharmacyResponse> getPharmacy(@PathVariable UUID id) {
    PharmacyResponse pharmacy = pharmacyApplicationService.getPharmacy(id);
    return ResponseEntity.ok(pharmacy);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updatePharmacy(
      @PathVariable UUID id, @Valid @RequestBody PharmacyUpdateRequest request) {
    pharmacyApplicationService.updatePharmacy(id, request);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePharmacy(@PathVariable UUID id) {
    pharmacyApplicationService.deletePharmacy(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/verify")
  public ResponseEntity<Void> verifyPharmacy(
      @PathVariable UUID id, @RequestParam String token) {
    pharmacyApplicationService.verifyPharmacy(id, token);
    return ResponseEntity.noContent().build();
  }
}