package com.ph.backoffice.domain.certifications;

import cm.fastrelays.common.domain.UuidBaseEntity;
import com.ph.backoffice.domain.certifications.model.CertificationRequestCreateData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "t_certification_request")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CertificationRequest extends UuidBaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "c_pharmacy_id")
  private Pharmacy pharmacy;

  @Column(name = "c_authorization_number")
  private String authorizationNumber;

  @Column(name = "c_tax_id")
  private String taxId;

  @Column(name = "c_legal_representative")
  private String legalRepresentative;

  @Column(name = "c_creation_date")
  private LocalDate creationDate;

  @Column(name = "c_authorization_document")
  private String authorizationDocument;

  @Column(name = "c_business_registry_document")
  private String businessRegistryDocument;

  @Column(name = "c_owner_id_recto_document")
  private String ownerIdRectoDocument;

  @Column(name = "c_owner_id_verso_document")
  private String ownerIdVersoDocument;

  @Column(name = "c_pharmacy_license_document")
  private String pharmacyLicenseDocument;

  @Column(name = "c_notes")
  private String notes;

  @Enumerated(EnumType.STRING)
  @Column(name = "c_status")
  private RequestStatus status;

  public static CertificationRequest create(
      Pharmacy pharmacy, CertificationRequestCreateData data) {
    return CertificationRequest.builder()
        .pharmacy(pharmacy)
        .authorizationNumber(data.authorizationNumber())
        .taxId(data.taxId())
        .legalRepresentative(data.legalRepresentative())
        .creationDate(data.creationDate())
        .authorizationDocument(data.authorizationDocument())
        .businessRegistryDocument(data.businessRegistryDocument())
        .ownerIdRectoDocument(data.ownerIdRectoDocument())
        .ownerIdVersoDocument(data.ownerIdVersoDocument())
        .pharmacyLicenseDocument(data.pharmacyLicenseDocument())
        .notes(data.notes())
        .status(RequestStatus.DRAFT)
        .build();
  }

  public enum RequestStatus {
    DRAFT,
    PENDING,
    APPROVED,
    REJECTED
  }
}
