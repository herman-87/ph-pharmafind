package com.ph.backoffice.domain.certifications.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record PharmacyResponse(
    UUID id,
    String name,
    String status,
    String city,
    String district,
    String locality,
    String fullAddress,
    Double latitude,
    Double longitude,
    Boolean is24h,
    Boolean deliveryAvailable,
    Integer deliveryRadiusKm,
    String logoUrl,
    Boolean isCertified,
    List<OpeningHour> openingHours,
    List<DutySchedule> dutySchedules,
    Map<String, String> paymentMethods,
    Map<String, String> socialLinks,
    LocalDateTime createdAt) {

  public record DutySchedule(
      LocalDate startDate, LocalDate endDate, String startTime, String endTime) {}
}
