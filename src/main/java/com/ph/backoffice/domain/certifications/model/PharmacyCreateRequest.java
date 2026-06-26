package com.ph.backoffice.domain.certifications.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record PharmacyCreateRequest(
    String name,
    String email,
    String phone,
    String registrationNumber,
    String taxId,
    String city,
    String district,
    String locality,
    String fullAddress,
    Double latitude,
    Double longitude,
    Boolean is24h,
    Boolean deliveryAvailable,
    Integer deliveryRadiusKm,
    List<OpeningHour> openingHours,
    List<DutySchedule> dutySchedules,
    Map<String, String> paymentMethods,
    Map<String, String> socialLinks) {

  public record DutySchedule(
      LocalDate startDate, LocalDate endDate, String startTime, String endTime) {}
}
