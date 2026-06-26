package com.ph.backoffice.domain.certifications.model;

import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record PharmacyData(
    String name,
    String email,
    String phone,
    String city,
    String quarter,
    String address,
    String gpsCoordinates,
    String registrationNumber,
    String taxId,
    Boolean is24h,
    Boolean deliveryAvailable,
    Integer deliveryRadiusKm,
    List<OpeningHour> openingHours,
    Map<String, String> paymentMethods,
    Map<String, String> socialLinks) {}
