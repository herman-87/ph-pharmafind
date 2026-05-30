package com.ph.backoffice.domain.certifications;

import cm.fastrelays.common.domain.UuidBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
  @JoinColumn(name = "c_pharmacy_id", nullable = false)
  private Pharmacy pharmacy;

  @Column(name = "c_document_url", nullable = false)
  private String documentUrl;

  @Column(name = "c_notes", length = 500)
  private String notes;

  @Enumerated(EnumType.STRING)
  @Column(name = "c_status", nullable = false, length = 20)
  private RequestStatus status;

  public static CertificationRequest create(Pharmacy pharmacy, String documentUrl, String notes) {
    return CertificationRequest.builder()
        .pharmacy(pharmacy)
        .documentUrl(documentUrl)
        .notes(notes)
        .status(RequestStatus.PENDING)
        .build();
  }

  public enum RequestStatus {
    PENDING,
    APPROVED,
    REJECTED
  }
}
