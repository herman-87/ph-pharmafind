package com.ph.backoffice.domain.certifications.model;

import java.time.LocalDate;

public record CertificationRequestCreateData(
    String authorizationNumber,
    String taxId,
    String legalRepresentative,
    LocalDate creationDate,
    String authorizationDocument,
    String businessRegistryDocument,
    String ownerIdRectoDocument,
    String ownerIdVersoDocument,
    String pharmacyLicenseDocument,
    String notes) {}
