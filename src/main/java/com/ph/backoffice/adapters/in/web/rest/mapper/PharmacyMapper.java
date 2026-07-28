package com.ph.backoffice.adapters.in.web.rest.mapper;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.DomainPage;
import com.ph.backoffice.domain.certifications.model.OpeningHour;
import com.ph.backoffice.domain.certifications.model.PharmacyCreateRequest;
import com.ph.backoffice.domain.certifications.model.PharmacyUpdateRequest;
import com.ph.pharmafind.generated.model.CreatePharmacyRequestDTO;
import com.ph.pharmafind.generated.model.OpeningHourDTO;
import com.ph.pharmafind.generated.model.PaymentMethodDTO;
import com.ph.pharmafind.generated.model.PharmacyIdResponseDTO;
import com.ph.pharmafind.generated.model.PharmacyPageResponseDTO;
import com.ph.pharmafind.generated.model.PharmacyResponseDTO;
import com.ph.pharmafind.generated.model.UpdatePharmacyRequestDTO;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PharmacyMapper {

  // ─── Create ─────────────────────────────────────────────────────────────────

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "name", source = "name")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "phone", source = "phone")
  @Mapping(target = "city", source = "city")
  @Mapping(target = "district", source = "district")
  @Mapping(target = "locality", source = "locality")
  @Mapping(target = "fullAddress", source = "fullAddress")
  @Mapping(target = "latitude", source = "latitude")
  @Mapping(target = "longitude", source = "longitude")
  @Mapping(target = "registrationNumber", source = "registrationNumber")
  @Mapping(target = "taxId", source = "taxId")
  @Mapping(target = "is24h", source = "is24h")
  @Mapping(target = "deliveryAvailable", source = "deliveryAvailable")
  @Mapping(target = "deliveryRadiusKm", source = "deliveryRadiusKm")
  @Mapping(target = "openingHours", source = "openingHours")
  @Mapping(target = "dutySchedules", source = "dutySchedules")
  @Mapping(
      target = "paymentMethods",
      source = "paymentMethods",
      qualifiedByName = "toPaymentMethodsMap")
  @Mapping(target = "socialLinks", source = "socialLinks")
  PharmacyCreateRequest toCreateRequest(CreatePharmacyRequestDTO dto);

  // ─── Update Request ─────────────────────────────────────────────────────────

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "name", source = "name")
  @Mapping(target = "city", source = "city")
  @Mapping(target = "district", source = "district")
  @Mapping(target = "locality", source = "locality")
  @Mapping(target = "fullAddress", source = "fullAddress")
  @Mapping(target = "latitude", source = "latitude")
  @Mapping(target = "longitude", source = "longitude")
  @Mapping(target = "is24h", source = "is24h")
  @Mapping(target = "deliveryAvailable", source = "deliveryAvailable")
  @Mapping(target = "deliveryRadiusKm", source = "deliveryRadiusKm")
  @Mapping(target = "openingHours", source = "openingHours")
  @Mapping(target = "dutySchedules", source = "dutySchedules")
  @Mapping(
      target = "paymentMethods",
      source = "paymentMethods",
      qualifiedByName = "toPaymentMethodsMap")
  @Mapping(target = "socialLinks", source = "socialLinks")
  PharmacyUpdateRequest toUpdateRequest(UpdatePharmacyRequestDTO dto);

  // ─── Partial Update (null-safe merge) ───────────────────────────────────────

  @BeanMapping(
      ignoreByDefault = true,
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "name", source = "name")
  @Mapping(target = "city", source = "city")
  @Mapping(target = "address", source = "fullAddress")
  @Mapping(target = "is24h", source = "is24h")
  @Mapping(target = "deliveryAvailable", source = "deliveryAvailable")
  @Mapping(target = "deliveryRadiusKm", source = "deliveryRadiusKm")
  @Mapping(target = "openingHours", source = "openingHours")
  @Mapping(target = "paymentMethods", source = "paymentMethods")
  @Mapping(target = "socialLinks", source = "socialLinks")
  void updatePharmacy(PharmacyUpdateRequest request, @MappingTarget Pharmacy pharmacy);

  @AfterMapping
  default void afterUpdatePharmacy(
      PharmacyUpdateRequest request, @MappingTarget Pharmacy pharmacy) {
    if (request.locality() != null && !request.locality().isBlank()) {
      pharmacy.setQuarter(request.locality());
    } else if (request.district() != null && !request.district().isBlank()) {
      pharmacy.setQuarter(request.district());
    }
    if (request.latitude() != null && request.longitude() != null) {
      pharmacy.setGpsCoordinates(request.latitude() + "," + request.longitude());
    }
  }

  // ─── ID Response ────────────────────────────────────────────────────────────

  default PharmacyIdResponseDTO toPharmacyIdResponseDTO(UUID id) {
    if (id == null) return null;
    return new PharmacyIdResponseDTO().id(id);
  }

  // ─── Page Response ──────────────────────────────────────────────────────────

  default PharmacyPageResponseDTO toPharmacyPageResponseDTO(DomainPage<Pharmacy> page) {
    return new PharmacyPageResponseDTO()
        .content(toPharmacyResponseDTO(page.content()))
        .page(page.page())
        .size(page.size())
        .totalElements(page.totalElements())
        .totalPages(page.totalPages());
  }

  // ─── Pharmacy → DTO (list) ──────────────────────────────────────────────────

  List<PharmacyResponseDTO> toPharmacyResponseDTO(List<Pharmacy> pharmacies);

  // ─── Pharmacy → DTO (single) ────────────────────────────────────────────────

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "phone", source = "phone")
  @Mapping(target = "city", source = "city")
  @Mapping(target = "district", source = "quarter")
  @Mapping(target = "locality", source = "quarter")
  @Mapping(target = "fullAddress", source = "address")
  @Mapping(target = "latitude", source = "gpsCoordinates", qualifiedByName = "extractLatitude")
  @Mapping(target = "longitude", source = "gpsCoordinates", qualifiedByName = "extractLongitude")
  @Mapping(target = "registrationNumber", source = "registrationNumber")
  @Mapping(target = "taxId", source = "taxId")
  @Mapping(target = "is24h", source = "is24h")
  @Mapping(target = "deliveryAvailable", source = "deliveryAvailable")
  @Mapping(target = "deliveryRadiusKm", source = "deliveryRadiusKm")
  @Mapping(target = "isCertified", source = "certified")
  @Mapping(target = "status", constant = "PENDING_VERIFICATION")
  @Mapping(target = "openingHours", source = "openingHours")
  @Mapping(target = "paymentMethods", source = "paymentMethods")
  @Mapping(target = "socialLinks", source = "socialLinks")
  @Mapping(target = "createdAt", source = "createdAt")
  @Mapping(target = "updatedAt", source = "updatedAt")
  PharmacyResponseDTO toPharmacyResponseDTO(Pharmacy pharmacy);

  // ─── QualifiedByName helpers ────────────────────────────────────────────────

  @Named("toPaymentMethodsMap")
  default Map<String, String> toPaymentMethodsMap(List<PaymentMethodDTO> paymentMethodDTOS) {
    if (paymentMethodDTOS == null) return Collections.emptyMap();
    Map<String, String> map = new HashMap<>();
    for (PaymentMethodDTO dto : paymentMethodDTOS) {
      map.put(dto.getType().name(), dto.getAccountNumber());
    }
    return map;
  }

  @Named("extractLatitude")
  default BigDecimal extractLatitude(String gpsCoordinates) {
    var value = extractCoordinate(gpsCoordinates, 0);
    return value != null ? BigDecimal.valueOf(value) : null;
  }

  @Named("extractLongitude")
  default BigDecimal extractLongitude(String gpsCoordinates) {
    var value = extractCoordinate(gpsCoordinates, 1);
    return value != null ? BigDecimal.valueOf(value) : null;
  }

  // ─── Type mapping helpers ───────────────────────────────────────────────────

  default OpeningHour toOpeningHour(OpeningHourDTO openingHourDTO) {
    if (openingHourDTO == null) return null;
    LocalTime openTime =
        openingHourDTO.getOpenTime() != null ? LocalTime.parse(openingHourDTO.getOpenTime()) : null;
    LocalTime closeTime =
        openingHourDTO.getCloseTime() != null
            ? LocalTime.parse(openingHourDTO.getCloseTime())
            : null;
    boolean isOpen = openingHourDTO.getIsClosed() == null || !openingHourDTO.getIsClosed();
    return new OpeningHour(
        DayOfWeek.valueOf(openingHourDTO.getDayOfWeek().name()), openTime, closeTime, isOpen);
  }

  default OpeningHourDTO toOpeningHourDTO(OpeningHour openingHour) {
    if (openingHour == null) return null;
    return new OpeningHourDTO()
        .dayOfWeek(OpeningHourDTO.DayOfWeekEnum.valueOf(openingHour.getDayOfWeek().name()))
        .openTime(openingHour.getOpenTime() != null ? openingHour.getOpenTime().toString() : null)
        .closeTime(
            openingHour.getCloseTime() != null ? openingHour.getCloseTime().toString() : null)
        .isClosed(!openingHour.isOpen());
  }

  default List<PaymentMethodDTO> toPaymentMethodDTOList(Map<String, String> paymentMethods) {
    if (paymentMethods == null) return Collections.emptyList();
    List<PaymentMethodDTO> list = new ArrayList<>();
    for (Map.Entry<String, String> entry : paymentMethods.entrySet()) {
      list.add(
          new PaymentMethodDTO()
              .type(PaymentMethodDTO.TypeEnum.fromValue(entry.getKey()))
              .accountNumber(entry.getValue()));
    }
    return list;
  }

  private static Double extractCoordinate(String gpsCoordinates, int index) {
    if (gpsCoordinates == null || gpsCoordinates.isBlank()) {
      return null;
    }
    var parts = gpsCoordinates.split(",", -1);
    if (parts.length <= index || parts[index].isBlank()) {
      return null;
    }
    return Double.valueOf(parts[index].trim());
  }
}
