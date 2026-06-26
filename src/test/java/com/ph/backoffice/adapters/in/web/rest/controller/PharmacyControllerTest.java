package com.ph.backoffice.adapters.in.web.rest.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.ph.backoffice.adapters.in.web.rest.exception.GlobalExceptionHandler;
import com.ph.backoffice.adapters.in.web.rest.mapper.PharmacyMapper;
import com.ph.backoffice.application.pharmacy.usecase.CreateCertificationRequestUseCase;
import com.ph.backoffice.application.pharmacy.usecase.CreatePharmacyUseCase;
import com.ph.backoffice.application.pharmacy.usecase.DeletePharmacyUseCase;
import com.ph.backoffice.application.pharmacy.usecase.GetPharmacyUseCase;
import com.ph.backoffice.application.pharmacy.usecase.ListCurrentUserPharmaciesUseCase;
import com.ph.backoffice.application.pharmacy.usecase.ListPharmaciesUseCase;
import com.ph.backoffice.application.pharmacy.usecase.UpdatePharmacyUseCase;
import com.ph.backoffice.application.pharmacy.usecase.VerifyPharmacyUseCase;
import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.exception.PharmacyNotFoundException;
import com.ph.pharmafind.generated.model.CreateCertificationRequestDTO;
import com.ph.pharmafind.generated.model.CreatePharmacyRequestDTO;
import com.ph.pharmafind.generated.model.OpeningHourDTO;
import com.ph.pharmafind.generated.model.PaymentMethodDTO;
import com.ph.pharmafind.generated.model.PharmacyIdResponseDTO;
import com.ph.pharmafind.generated.model.PharmacyPageResponseDTO;
import com.ph.pharmafind.generated.model.PharmacyResponseDTO;
import com.ph.pharmafind.generated.model.UpdatePharmacyRequestDTO;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class PharmacyControllerTest {

  @Mock private CreatePharmacyUseCase createPharmacyUseCase;
  @Mock private ListPharmaciesUseCase listPharmaciesUseCase;
  @Mock private GetPharmacyUseCase getPharmacyUseCase;
  @Mock private UpdatePharmacyUseCase updatePharmacyUseCase;
  @Mock private DeletePharmacyUseCase deletePharmacyUseCase;
  @Mock private VerifyPharmacyUseCase verifyPharmacyUseCase;
  @Mock private CreateCertificationRequestUseCase createCertificationRequestUseCase;
  @Mock private ListCurrentUserPharmaciesUseCase listCurrentUserPharmaciesUseCase;
  @Mock private PharmacyMapper pharmacyMapper;

  @BeforeEach
  void setup() {
    PharmacyController pharmacyController =
        new PharmacyController(
            createPharmacyUseCase,
            listPharmaciesUseCase,
            getPharmacyUseCase,
            updatePharmacyUseCase,
            deletePharmacyUseCase,
            verifyPharmacyUseCase,
            createCertificationRequestUseCase,
            listCurrentUserPharmaciesUseCase,
            pharmacyMapper);

    RestAssuredMockMvc.standaloneSetup(pharmacyController, new GlobalExceptionHandler());
  }

  // ─── Helpers ──────────────────────────────────────────────────────────────

  private CreatePharmacyRequestDTO validCreateBody() {
    return new CreatePharmacyRequestDTO()
        .name("Pharmacie Test")
        .email("test@pharmacie.com")
        .phone("+237699000001")
        .city("Yaoundé")
        .district("Bastos")
        .fullAddress("123 rue de la Paix")
        .latitude(3.866667)
        .longitude(11.516667)
        .openingHours(
            List.of(
                new OpeningHourDTO()
                    .dayOfWeek(OpeningHourDTO.DayOfWeekEnum.MONDAY)
                    .openTime("08:00")
                    .closeTime("20:00")
                    .isClosed(false)))
        .paymentMethods(List.of(new PaymentMethodDTO().type(PaymentMethodDTO.TypeEnum.CASH)));
  }

  // ─── POST /me/pharmacies ──────────────────────────────────────────────────

  @Test
  void createPharmacy_returns201WithId() {
    UUID pharmacyId = UUID.randomUUID();
    when(pharmacyMapper.toCreateRequest(any())).thenReturn(null);
    when(createPharmacyUseCase.execute(any())).thenReturn(pharmacyId);
    when(pharmacyMapper.toPharmacyIdResponseDTO(pharmacyId))
        .thenReturn(new PharmacyIdResponseDTO().id(pharmacyId));

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(validCreateBody())
        .post("/me/pharmacies")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("id", equalTo(pharmacyId.toString()));

    verify(createPharmacyUseCase).execute(any());
  }

  // ─── GET /public/pharmacies ───────────────────────────────────────────────

  @Test
  void listPharmacies_returns200() {
    when(listPharmaciesUseCase.execute(any(), any(), any())).thenReturn(new PageImpl<>(List.of()));
    when(pharmacyMapper.toPharmacyPageResponseDTO(any()))
        .thenReturn(new PharmacyPageResponseDTO().content(List.of()).totalElements(0L));

    RestAssuredMockMvc.given()
        .queryParam("page", 0)
        .queryParam("size", 10)
        .get("/public/pharmacies")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("totalElements", equalTo(0));

    verify(listPharmaciesUseCase).execute(any(), any(), any());
  }

  // ─── GET /public/pharmacies/{id} ─────────────────────────────────────────

  @Test
  void getPharmacy_returns200_whenExists() {
    UUID id = UUID.randomUUID();
    when(getPharmacyUseCase.execute(id)).thenReturn(new Pharmacy());
    when(pharmacyMapper.toPharmacyResponseDTO(any(Pharmacy.class)))
        .thenReturn(new PharmacyResponseDTO().name("Pharmacie Test"));

    RestAssuredMockMvc.given()
        .get("/public/pharmacies/{id}", id)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("name", equalTo("Pharmacie Test"));

    verify(getPharmacyUseCase).execute(id);
  }

  @Test
  void getPharmacy_returns404_whenNotFound() {
    UUID id = UUID.randomUUID();
    when(getPharmacyUseCase.execute(id)).thenThrow(new PharmacyNotFoundException(id));

    RestAssuredMockMvc.given()
        .get("/public/pharmacies/{id}", id)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  // ─── POST /public/pharmacies/{id}/verify ─────────────────────────────────

  @Test
  void verifyPharmacy_returns204_whenValid() {
    UUID id = UUID.randomUUID();
    doNothing().when(verifyPharmacyUseCase).execute(id, "token123");

    RestAssuredMockMvc.given()
        .queryParam("token", "token123")
        .post("/public/pharmacies/{id}/verify", id)
        .then()
        .statusCode(HttpStatus.NO_CONTENT.value());

    verify(verifyPharmacyUseCase).execute(id, "token123");
  }

  @Test
  void verifyPharmacy_returns404_whenNotFound() {
    UUID id = UUID.randomUUID();
    doThrow(new PharmacyNotFoundException(id)).when(verifyPharmacyUseCase).execute(any(), any());

    RestAssuredMockMvc.given()
        .queryParam("token", "token123")
        .post("/public/pharmacies/{id}/verify", id)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  // ─── GET /me/pharmacies ───────────────────────────────────────────────────

  @Test
  void getMyPharmacies_returns200() {
    when(listCurrentUserPharmaciesUseCase.execute(any())).thenReturn(new PageImpl<>(List.of()));
    when(pharmacyMapper.toPharmacyPageResponseDTO(any()))
        .thenReturn(new PharmacyPageResponseDTO().content(List.of()).totalElements(0L));

    RestAssuredMockMvc.given()
        .queryParam("page", 0)
        .queryParam("size", 10)
        .get("/me/pharmacies")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("totalElements", equalTo(0));

    verify(listCurrentUserPharmaciesUseCase).execute(any());
  }

  // ─── PUT /me/pharmacies/{id} ──────────────────────────────────────────────

  @Test
  void updatePharmacy_returns204() {
    UUID id = UUID.randomUUID();
    when(pharmacyMapper.toUpdateRequest(any())).thenReturn(null);
    doNothing().when(updatePharmacyUseCase).execute(eq(id), any());

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(new UpdatePharmacyRequestDTO().name("Updated"))
        .put("/me/pharmacies/{id}", id)
        .then()
        .statusCode(HttpStatus.NO_CONTENT.value());

    verify(updatePharmacyUseCase).execute(eq(id), any());
  }

  @Test
  void updatePharmacy_returns404_whenNotFound() {
    UUID id = UUID.randomUUID();
    when(pharmacyMapper.toUpdateRequest(any())).thenReturn(null);
    doThrow(new PharmacyNotFoundException(id)).when(updatePharmacyUseCase).execute(any(), any());

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(new UpdatePharmacyRequestDTO().name("Updated"))
        .put("/me/pharmacies/{id}", id)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  // ─── DELETE /me/pharmacies/{id} ───────────────────────────────────────────

  @Test
  void deletePharmacy_returns204() {
    UUID id = UUID.randomUUID();
    doNothing().when(deletePharmacyUseCase).execute(id);

    RestAssuredMockMvc.given()
        .delete("/me/pharmacies/{id}", id)
        .then()
        .statusCode(HttpStatus.NO_CONTENT.value());

    verify(deletePharmacyUseCase).execute(id);
  }

  @Test
  void deletePharmacy_returns404_whenNotFound() {
    UUID id = UUID.randomUUID();
    doThrow(new PharmacyNotFoundException(id)).when(deletePharmacyUseCase).execute(any());

    RestAssuredMockMvc.given()
        .delete("/me/pharmacies/{id}", id)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  // ─── POST /me/pharmacies/{id}/certification-requests ─────────────────────

  @Test
  void createCertificationRequest_returns201() {
    UUID pharmacyId = UUID.randomUUID();
    UUID certifId = UUID.randomUUID();
    when(createCertificationRequestUseCase.execute(eq(pharmacyId), any(), any()))
        .thenReturn(certifId);

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(new CreateCertificationRequestDTO().documentUrl("https://example.com/doc.pdf"))
        .post("/me/pharmacies/{id}/certification-requests", pharmacyId)
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("id", equalTo(certifId.toString()));

    verify(createCertificationRequestUseCase).execute(eq(pharmacyId), any(), any());
  }

  @Test
  void createCertificationRequest_returns404_whenNotFound() {
    UUID pharmacyId = UUID.randomUUID();
    doThrow(new PharmacyNotFoundException(pharmacyId))
        .when(createCertificationRequestUseCase)
        .execute(any(), any(), any());

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(new CreateCertificationRequestDTO().documentUrl("https://example.com/doc.pdf"))
        .post("/me/pharmacies/{id}/certification-requests", pharmacyId)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }
}
