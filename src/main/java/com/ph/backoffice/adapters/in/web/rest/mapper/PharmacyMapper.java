package com.ph.backoffice.adapters.in.web.rest.mapper;

import com.ph.backoffice.domain.certifications.Pharmacy;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PharmacyMapper {

  @Mapping(
      target = "paymentMethods",
      expression = "java(toPaymentMethodsMap(dto.getPaymentMethods()))")
  PharmacyCreateRequest toCreateRequest(CreatePharmacyRequestDTO dto);

  @Mapping(
      target = "paymentMethods",
      expression = "java(toPaymentMethodsMap(dto.getPaymentMethods()))")
  PharmacyUpdateRequest toUpdateRequest(UpdatePharmacyRequestDTO dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "quarter", expression = "java(request.locality() != null ? request.locality() : request.district())")
  @Mapping(target = "address", source = "fullAddress")
  @Mapping(target = "gpsCoordinates", expression = "java(request.latitude() != null && request.longitude() != null ? request.latitude() + \",\" + request.longitude() : null)")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "owner", ignore = true)
  @Mapping(target = "certified", ignore = true)
  void updatePharmacy(PharmacyUpdateRequest request, @MappingTarget Pharmacy pharmacy);

  PharmacyIdResponseDTO toPharmacyIdResponseDTO(UUID id);

  default PharmacyPageResponseDTO toPharmacyPageResponseDTO(
      org.springframework.data.domain.Page<Pharmacy> page) {
    return new PharmacyPageResponseDTO()
        .content(toPharmacyResponseDTO(page.getContent()))
        .page(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages());
  }

  List<PharmacyResponseDTO> toPharmacyResponseDTO(List<Pharmacy> pharmacies);

  @Mapping(target = "status", constant = "PENDING_VERIFICATION")
  @Mapping(target = "district", source = "quarter")
  @Mapping(target = "locality", source = "quarter")
  @Mapping(target = "fullAddress", source = "address")
  @Mapping(target = "latitude", expression = "java(extractLatitude(pharmacy.getGpsCoordinates()))")
  @Mapping(
      target = "longitude",
      expression = "java(extractLongitude(pharmacy.getGpsCoordinates()))")
  @Mapping(target = "is24h", constant = "false")
  @Mapping(target = "deliveryAvailable", constant = "false")
  @Mapping(target = "deliveryRadiusKm", ignore = true)
  @Mapping(target = "logoUrl", ignore = true)
  @Mapping(target = "isCertified", source = "certified")
  @Mapping(target = "openingHours", expression = "java(java.util.Collections.emptyList())")
  @Mapping(target = "dutySchedules", expression = "java(java.util.Collections.emptyList())")
  @Mapping(target = "paymentMethods", expression = "java(java.util.Collections.emptyList())")
  @Mapping(target = "socialLinks", expression = "java(new java.util.HashMap<>())")
  PharmacyResponseDTO toPharmacyResponseDTO(Pharmacy pharmacy);

  default OpeningHour toOpeningHour(OpeningHourDTO dto) {
    if (dto == null) return null;
    LocalTime openTime = dto.getOpenTime() != null ? LocalTime.parse(dto.getOpenTime()) : null;
    LocalTime closeTime = dto.getCloseTime() != null ? LocalTime.parse(dto.getCloseTime()) : null;
    boolean isOpen = dto.getIsClosed() == null || !dto.getIsClosed();
    return new OpeningHour(
        DayOfWeek.valueOf(dto.getDayOfWeek().name()), openTime, closeTime, isOpen);
  }

  default Map<String, String> toPaymentMethodsMap(List<PaymentMethodDTO> dtos) {
    if (dtos == null) return null;
    Map<String, String> map = new HashMap<>();
    for (PaymentMethodDTO dto : dtos) {
      if (dto.getType() != null) {
        map.put(dto.getType().name(), dto.getAccountNumber());
      }
    }
    return map;
  }

  default BigDecimal extractLatitude(String gpsCoordinates) {
    var value = extractCoordinate(gpsCoordinates, 0);
    return value != null ? BigDecimal.valueOf(value) : null;
  }

  default BigDecimal extractLongitude(String gpsCoordinates) {
    var value = extractCoordinate(gpsCoordinates, 1);
    return value != null ? BigDecimal.valueOf(value) : null;
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
