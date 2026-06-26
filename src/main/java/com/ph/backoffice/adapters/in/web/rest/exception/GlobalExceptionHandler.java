package com.ph.backoffice.adapters.in.web.rest.exception;

import com.ph.backoffice.domain.certifications.exception.DomainException;
import com.ph.pharmafind.generated.model.ApiErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ApiErrorResponseDTO> handleDomainException(
      DomainException ex, HttpServletRequest request) {
    HttpStatus status =
        switch (ex.getErrorCode()) {
          case PHARMACY_ERR_003, PHARMACY_ERR_004 -> HttpStatus.CONFLICT;
          case PHARMACY_ERR_002, PHARMACY_ERR_007 -> HttpStatus.NOT_FOUND;
          case PHARMACY_ERR_006 -> HttpStatus.FORBIDDEN;
          default -> HttpStatus.BAD_REQUEST;
        };

    ApiErrorResponseDTO errorResponse =
        new ApiErrorResponseDTO()
            .timestamp(LocalDateTime.now())
            .status(status.value())
            .error(status.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .errorCode(ex.getErrorCode().getCode());

    return ResponseEntity.status(status).body(errorResponse);
  }
}
