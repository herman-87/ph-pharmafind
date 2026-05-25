package com.ph.pharmafind.application.pharmacy.dto;

import jakarta.validation.constraints.Size;
import java.util.List;

public record PharmacyUpdateRequest(
    @Size(min = 2, max = 200) String name,
    @Size(min = 2, max = 100) String city,
    @Size(min = 2, max = 100) String quarter,
    @Size(min = 2, max = 100) String locality,
    @Size(min = 5, max = 300) String fullAddress,
    Double latitude,
    Double longitude,
    Boolean is24h,
    Boolean deliveryAvailable,
    Integer deliveryRadiusKm,
    List<OpeningHourDTO> openingHours,
    List<DutyScheduleDTO> dutySchedules,
    List<PaymentMethodDTO> paymentMethods,
    List<SocialLinkDTO> socialLinks) {

  public record OpeningHourDTO(
      String dayOfWeek,
      String openTime,
      String closeTime,
      Boolean isClosed) {}

  public record DutyScheduleDTO(
      String startDate,
      String endDate,
      String startTime,
      String endTime) {}

  public enum PaymentMethodDTO {
    CASH, MTN_MOMO, ORANGE_MONEY, VISA, MASTERCARD
  }

  public enum SocialLinkTypeDTO {
    FACEBOOK, INSTAGRAM, WHATSAPP, WEBSITE
  }

  public record SocialLinkDTO(SocialLinkTypeDTO type, String value) {}
}