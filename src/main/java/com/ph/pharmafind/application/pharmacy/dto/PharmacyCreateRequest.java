package com.ph.pharmafind.application.pharmacy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;

public record PharmacyCreateRequest(
    @NotBlank @Size(min = 2, max = 200) String pharmacyName,
    
    @NotBlank @Size(min = 2, max = 100) String ownerFirstName,
    
    @NotBlank @Size(min = 2, max = 100) String ownerLastName,
    
    @NotBlank @Email @Size(max = 190) String email,
    
    @NotBlank @Pattern(regexp = "^\\+237[6-9]\\d{7,8}$") String phone,
    
    @NotBlank @Size(min = 2, max = 100) String city,
    
    @NotBlank @Size(min = 2, max = 100) String quarter,
    
    @NotBlank @Size(min = 5, max = 300) String address,
    
    @NotNull Double gpsLatitude,
    
    @NotNull Double gpsLongitude,
    
    @NotNull Map<String, OpeningHours> openingHours,
    
    Map<String, OpeningHours> onCallHours,
    
    @Size(min = 1) List<PaymentMethod> paymentMethods,
    
    Map<String, String> socialNetworks,
    
    @NotBlank @Size(min = 8, max = 100) String password,
    
    @NotBlank String confirmPassword
) {
    public record OpeningHours(String open, String close) {}
    
    public enum PaymentMethod {
        MTN_MOMO, ORANGE_MONEY, CASH, CREDIT_CARD
    }
}