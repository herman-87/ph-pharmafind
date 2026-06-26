package com.ph.backoffice.adapters.in.web.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.OpeningHour;
import com.ph.backoffice.domain.certifications.model.PharmacyUpdateRequest;
import com.ph.pharmafind.generated.model.OpeningHourDTO;
import com.ph.pharmafind.generated.model.PaymentMethodDTO;
import com.ph.pharmafind.generated.model.PharmacyResponseDTO;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PharmacyMapperTest {

  private final PharmacyMapper mapper = new PharmacyMapperImpl();

  private Pharmacy existingPharmacy;

  @BeforeEach
  void setUp() {
    existingPharmacy = Pharmacy.builder()
        .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
        .name("Pharmacie Originale")
        .email("contact@pharmacie.cm")
        .phone("+237699000001")
        .city("Douala")
        .quarter("Bonanjo")
        .address("123 Rue de la Paix")
        .gpsCoordinates("4.05,9.70")
        .registrationNumber("REG-001")
        .taxId("TAX-001")
        .is24h(false)
        .deliveryAvailable(true)
        .deliveryRadiusKm(10)
        .certified(true)
        .openingHours(List.of(
            new OpeningHour(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0), true)))
        .paymentMethods(Map.of("CASH", "CASH"))
        .socialLinks(Map.of("facebook", "fb.com/pharma"))
        .createdAt(LocalDateTime.of(2025, 1, 1, 10, 0))
        .updatedAt(LocalDateTime.of(2025, 6, 1, 14, 30))
        .build();
  }

  // ─── updatePharmacy ─────────────────────────────────────────────────────────

  @Test
  void shouldIgnoreNullFields() {
    var request = new PharmacyUpdateRequest(null, null, null, null, null, null, null, null,
        null, null, null, null, null, null);

    mapper.updatePharmacy(request, existingPharmacy);

    assertThat(existingPharmacy.getName()).isEqualTo("Pharmacie Originale");
    assertThat(existingPharmacy.getCity()).isEqualTo("Douala");
    assertThat(existingPharmacy.getQuarter()).isEqualTo("Bonanjo");
    assertThat(existingPharmacy.getAddress()).isEqualTo("123 Rue de la Paix");
    assertThat(existingPharmacy.getGpsCoordinates()).isEqualTo("4.05,9.70");
    assertThat(existingPharmacy.getIs24h()).isFalse();
    assertThat(existingPharmacy.getDeliveryAvailable()).isTrue();
    assertThat(existingPharmacy.getDeliveryRadiusKm()).isEqualTo(10);
    assertThat(existingPharmacy.getOpeningHours()).hasSize(1);
    assertThat(existingPharmacy.getPaymentMethods()).containsEntry("CASH", "CASH");
    assertThat(existingPharmacy.getSocialLinks()).containsEntry("facebook", "fb.com/pharma");
  }

  @Test
  void shouldCopyNonNullFields() {
    var request = new PharmacyUpdateRequest(
        "Pharmacie Mise à Jour", "Yaoundé", null, "Mvan", "456 Avenue de la République",
        3.87, 11.52, true, false, 20, null, null, null, null);

    mapper.updatePharmacy(request, existingPharmacy);

    assertThat(existingPharmacy.getName()).isEqualTo("Pharmacie Mise à Jour");
    assertThat(existingPharmacy.getCity()).isEqualTo("Yaoundé");
    assertThat(existingPharmacy.getQuarter()).isEqualTo("Mvan");
    assertThat(existingPharmacy.getAddress()).isEqualTo("456 Avenue de la République");
    assertThat(existingPharmacy.getGpsCoordinates()).isEqualTo("3.87,11.52");
    assertThat(existingPharmacy.getIs24h()).isTrue();
    assertThat(existingPharmacy.getDeliveryAvailable()).isFalse();
    assertThat(existingPharmacy.getDeliveryRadiusKm()).isEqualTo(20);
  }

  @Test
  void shouldResolveQuarterFromDistrict_whenLocalityIsNull() {
    var request = new PharmacyUpdateRequest(null, null, "Bastos", null, null, null, null,
        null, null, null, null, null, null, null);

    mapper.updatePharmacy(request, existingPharmacy);

    assertThat(existingPharmacy.getQuarter()).isEqualTo("Bastos");
  }

  @Test
  void shouldUseLocalityOverDistrict() {
    var request = new PharmacyUpdateRequest(null, null, "Bastos", "Mvan", null, null, null,
        null, null, null, null, null, null, null);

    mapper.updatePharmacy(request, existingPharmacy);

    assertThat(existingPharmacy.getQuarter()).isEqualTo("Mvan");
  }

  @Test
  void shouldNotUpdateGpsCoordinates_whenOnlyLatitudeProvided() {
    var request = new PharmacyUpdateRequest(null, null, null, null, null, 3.87, null,
        null, null, null, null, null, null, null);

    mapper.updatePharmacy(request, existingPharmacy);

    assertThat(existingPharmacy.getGpsCoordinates()).isEqualTo("4.05,9.70");
  }

  @Test
  void shouldNotUpdateGpsCoordinates_whenOnlyLongitudeProvided() {
    var request = new PharmacyUpdateRequest(null, null, null, null, null, null, 11.52,
        null, null, null, null, null, null, null);

    mapper.updatePharmacy(request, existingPharmacy);

    assertThat(existingPharmacy.getGpsCoordinates()).isEqualTo("4.05,9.70");
  }

  @Test
  void shouldIgnoreIdOwnerAndCertified() {
    Pharmacy pharmacyWithDefaults = Pharmacy.builder()
        .id(UUID.randomUUID())
        .name("Test")
        .city("City")
        .quarter("Quarter")
        .address("Address")
        .gpsCoordinates("0.0,0.0")
        .build();

    var request = new PharmacyUpdateRequest("New Name", null, null, null, null, null, null,
        null, null, null, null, null, null, null);

    mapper.updatePharmacy(request, pharmacyWithDefaults);

    assertThat(pharmacyWithDefaults.getId()).isNotNull();
    assertThat(pharmacyWithDefaults.getOwner()).isNull();
    assertThat(pharmacyWithDefaults.isCertified()).isFalse();
  }

  // ─── toPharmacyResponseDTO ──────────────────────────────────────────────────

  @Test
  void shouldMapAllFieldsToResponseDTO() {
    PharmacyResponseDTO dto = mapper.toPharmacyResponseDTO(existingPharmacy);

    assertThat(dto).satisfies(d -> {
      assertThat(d.getId()).isEqualTo(existingPharmacy.getId());
      assertThat(d.getName()).isEqualTo("Pharmacie Originale");
      assertThat(d.getEmail()).isEqualTo("contact@pharmacie.cm");
      assertThat(d.getPhone()).isEqualTo("+237699000001");
      assertThat(d.getCity()).isEqualTo("Douala");
      assertThat(d.getDistrict()).isEqualTo("Bonanjo");
      assertThat(d.getLocality()).isEqualTo("Bonanjo");
      assertThat(d.getFullAddress()).isEqualTo("123 Rue de la Paix");
      assertThat(d.getLatitude()).isEqualByComparingTo(BigDecimal.valueOf(4.05));
      assertThat(d.getLongitude()).isEqualByComparingTo(BigDecimal.valueOf(9.70));
      assertThat(d.getRegistrationNumber()).isEqualTo("REG-001");
      assertThat(d.getTaxId()).isEqualTo("TAX-001");
      assertThat(d.getIs24h()).isFalse();
      assertThat(d.getDeliveryAvailable()).isTrue();
      assertThat(d.getDeliveryRadiusKm()).isEqualTo(10);
      assertThat(d.getIsCertified()).isTrue();
      assertThat(d.getStatus()).isEqualTo(PharmacyResponseDTO.StatusEnum.PENDING_VERIFICATION);
      assertThat(d.getCreatedAt()).isEqualTo(LocalDateTime.of(2025, Month.JANUARY, 1, 10, 0));
      assertThat(d.getUpdatedAt()).isEqualTo(LocalDateTime.of(2025, Month.JUNE, 1, 14, 30));
    });
  }

  @Test
  void shouldMapSocialLinks() {
    PharmacyResponseDTO dto = mapper.toPharmacyResponseDTO(existingPharmacy);

    assertThat(dto.getSocialLinks()).containsEntry("facebook", "fb.com/pharma");
  }

  @Test
  void shouldMapDutySchedulesAsEmpty() {
    PharmacyResponseDTO dto = mapper.toPharmacyResponseDTO(existingPharmacy);

    assertThat(dto.getDutySchedules()).isEmpty();
  }

  @Test
  void shouldMapLogoUrlAsNull() {
    PharmacyResponseDTO dto = mapper.toPharmacyResponseDTO(existingPharmacy);

    assertThat(dto.getLogoUrl()).isNull();
  }

  // ─── toPharmacyIdResponseDTO ────────────────────────────────────────────────

  @Test
  void shouldMapIdToResponse() {
    UUID id = UUID.randomUUID();
    var dto = mapper.toPharmacyIdResponseDTO(id);

    assertThat(dto.getId()).isEqualTo(id);
  }
}
