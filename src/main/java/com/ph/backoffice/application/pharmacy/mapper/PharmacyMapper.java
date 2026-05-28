package com.ph.backoffice.application.pharmacy.mapper;

import com.ph.backoffice.application.pharmacy.dto.PharmacyCreateRequest;
import com.ph.backoffice.application.pharmacy.dto.PharmacyResponse;
import com.ph.backoffice.application.pharmacy.dto.PharmacyUpdateRequest;
import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.pharmafind.generated.model.CreatePharmacyRequestDTO;
import com.ph.pharmafind.generated.model.OpeningHourDTO;
import com.ph.pharmafind.generated.model.PaymentMethodDTO;
import com.ph.pharmafind.generated.model.PharmacyIdResponseDTO;
import com.ph.pharmafind.generated.model.PharmacyPageResponseDTO;
import com.ph.pharmafind.generated.model.PharmacyResponseDTO;
import com.ph.pharmafind.generated.model.SocialLinkDTO;
import com.ph.pharmafind.generated.model.UpdatePharmacyRequestDTO;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PharmacyMapper {

  PharmacyCreateRequest toCreateRequest(CreatePharmacyRequestDTO dto);

  PharmacyUpdateRequest toUpdateRequest(UpdatePharmacyRequestDTO dto);

  PharmacyIdResponseDTO toPharmacyIdResponseDTO(UUID id);

  default PharmacyPageResponseDTO toPharmacyPageResponseDTO(
      org.springframework.data.domain.Page<PharmacyResponse> page) {
    return new PharmacyPageResponseDTO()
        .content(toPharmacyResponseDTO(page.getContent()))
        .page(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages());
  }

  List<PharmacyResponseDTO> toPharmacyResponseDTO(List<PharmacyResponse> responses);

  @Mapping(target = "status", expression = "java(mapStatus(response.status()))")
  PharmacyResponseDTO toPharmacyResponseDTO(PharmacyResponse response);

  default PharmacyResponseDTO.StatusEnum mapStatus(String status) {
    return status == null ? null : PharmacyResponseDTO.StatusEnum.fromValue(status);
  }

  default Double map(BigDecimal value) {
    return value == null ? null : value.doubleValue();
  }

  default String map(PaymentMethodDTO.TypeEnum type) {
    return type == null ? null : type.getValue();
  }

  default String mapUpdatePaymentMethodType(PaymentMethodDTO.TypeEnum type) {
    return type == null ? null : type.getValue();
  }

  default PharmacyCreateRequest.PaymentType mapCreatePaymentType(PaymentMethodDTO.TypeEnum type) {
    return type == null ? null : PharmacyCreateRequest.PaymentType.valueOf(type.getValue());
  }

  default PharmacyUpdateRequest.PaymentType mapUpdatePaymentType(PaymentMethodDTO.TypeEnum type) {
    return type == null ? null : PharmacyUpdateRequest.PaymentType.valueOf(type.getValue());
  }

  default PharmacyCreateRequest.SocialLinkTypeDTO mapCreateSocialLinkType(
      SocialLinkDTO.TypeEnum type) {
    return type == null ? null : PharmacyCreateRequest.SocialLinkTypeDTO.valueOf(type.getValue());
  }

  default PharmacyUpdateRequest.SocialLinkTypeDTO mapUpdateSocialLinkType(
      SocialLinkDTO.TypeEnum type) {
    return type == null ? null : PharmacyUpdateRequest.SocialLinkTypeDTO.valueOf(type.getValue());
  }

  default List<OpeningHourDTO> emptyOpeningHours() {
    return Collections.emptyList();
  }

  default List<com.ph.pharmafind.generated.model.DutyScheduleDTO> emptyDutySchedules() {
    return Collections.emptyList();
  }

  default List<PaymentMethodDTO> emptyPaymentMethods() {
    return Collections.emptyList();
  }

  default List<SocialLinkDTO> emptySocialLinks() {
    return Collections.emptyList();
  }

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
  @Mapping(target = "socialLinks", expression = "java(java.util.Collections.emptyList())")
  PharmacyResponse toResponse(Pharmacy pharmacy);

  default Double extractLatitude(String gpsCoordinates) {
    return extractCoordinate(gpsCoordinates, 0);
  }

  default Double extractLongitude(String gpsCoordinates) {
    return extractCoordinate(gpsCoordinates, 1);
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
