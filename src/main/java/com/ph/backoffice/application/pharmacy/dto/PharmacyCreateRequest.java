package com.ph.backoffice.application.pharmacy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record PharmacyCreateRequest(
    @NotBlank @Size(min = 2, max = 60) String firstName,
    @NotBlank @Size(min = 2, max = 60) String lastName,
    @NotBlank @Email @Size(max = 190) String email,
    @NotBlank @Pattern(regexp = "^\\+237[6-9]\\d{7,8}$") String phone,
    @NotBlank @Size(min = 8, max = 100) String password,
    @NotBlank String confirmPassword,
    @NotBlank @Size(min = 2, max = 120) String name,
    String registrationNumber,
    String taxId,
    @NotBlank @Size(min = 2, max = 100) String city,
    @NotBlank @Size(min = 2, max = 100) String district,
    @Size(min = 2, max = 100) String locality,
    @NotBlank @Size(min = 5, max = 255) String fullAddress,
    @NotNull Double latitude,
    @NotNull Double longitude,
    Boolean is24h,
    Boolean deliveryAvailable,
    @Min(1) Integer deliveryRadiusKm,
    @NotNull @Size(min = 1) List<OpeningHourDTO> openingHours,
    List<DutyScheduleDTO> dutySchedules,
    @NotNull @Size(min = 1) List<PaymentMethodDTO> paymentMethods,
    List<SocialLinkDTO> socialLinks) {
  public record OpeningHourDTO(
      @NotBlank String dayOfWeek, String openTime, String closeTime, Boolean isClosed) {}

  public record DutyScheduleDTO(
      @NotBlank String startDate,
      @NotBlank String endDate,
      @NotBlank String startTime,
      @NotBlank String endTime) {}

  public record PaymentMethodDTO(@NotNull PaymentType type, String accountNumber) {}

  public enum PaymentType {
    CASH,
    MTN_MOMO,
    ORANGE_MONEY,
    VISA,
    MASTERCARD
  }

  public record SocialLinkDTO(@NotNull SocialLinkTypeDTO type, @NotBlank String value) {}

  public enum SocialLinkTypeDTO {
    FACEBOOK,
    INSTAGRAM,
    WHATSAPP,
    WEBSITE
  }
}
