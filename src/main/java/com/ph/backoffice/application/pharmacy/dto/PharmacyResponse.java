package com.ph.backoffice.application.pharmacy.dto;

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
    List<OpeningHourDTO> openingHours,
    List<DutyScheduleDTO> dutySchedules,
    List<PaymentMethodDTO> paymentMethods,
    List<SocialLinkDTO> socialLinks,
    LocalDateTime createdAt) {

  public record OpeningHourDTO(
      String dayOfWeek, String openTime, String closeTime, Boolean isClosed) {}

  public record DutyScheduleDTO(
      LocalDate startDate, LocalDate endDate, String startTime, String endTime) {}

  public record PaymentMethodDTO(String type, String accountNumber) {}

  public record SocialLinkDTO(String type, String value) {}
}
