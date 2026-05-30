package com.ph.backoffice.domain.certifications.model;

import java.util.List;

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
    List<PaymentMethod> paymentMethods,
    List<SocialLink> socialLinks) {

  public record OpeningHour(String dayOfWeek, String openTime, String closeTime, Boolean isClosed) {}

  public record DutySchedule(String startDate, String endDate, String startTime, String endTime) {}

  public record PaymentMethod(String type, String accountNumber) {}

  public record SocialLink(String type, String value) {}
}
