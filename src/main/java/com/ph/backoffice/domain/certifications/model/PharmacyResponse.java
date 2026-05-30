package com.ph.backoffice.domain.certifications.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    List<PaymentMethod> paymentMethods,
    List<SocialLink> socialLinks,
    LocalDateTime createdAt) {

  public record OpeningHour(String dayOfWeek, String openTime, String closeTime, Boolean isClosed) {}

  public record DutySchedule(LocalDate startDate, LocalDate endDate, String startTime, String endTime) {}

  public record PaymentMethod(String type, String accountNumber) {}

  public record SocialLink(String type, String value) {}
}
