package cm.fastrelays.common.api;

import cm.fastrelays.common.exception.ApiException;
import cm.fastrelays.common.exception.ConflictException;
import cm.fastrelays.common.exception.ExternalServiceCallException;
import cm.fastrelays.common.exception.InternalServerError;
import cm.fastrelays.common.exception.InvalidEmailAddressException;
import cm.fastrelays.common.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

  private ResponseEntity<Map<String, Object>> errorResponse(
      HttpStatus status, String message, String details, Exception ex, HttpServletRequest request) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", message);
    body.put("details", details);
    body.put("exceptionType", ex.getClass().getSimpleName());
    body.put("path", request.getRequestURI());
    body.put("cause", safeMessage(ex.getCause()));
    body.put("rootCause", safeMessage(rootCause(ex)));

    if (ex instanceof ApiException apiEx) {
      body.put("errorCode", apiEx.getErrorCode().code());
    }

    return ResponseEntity.status(status).body(body);
  }

  @ExceptionHandler({HttpClientErrorException.class})
  public ResponseEntity<Map<String, Object>> handleHttpClientError(
      HttpClientErrorException ex, HttpServletRequest request) {
    String service = extractServiceName(ex);

    if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
      log.error(
          "FORBIDDEN: Service {} does not have required permissions. "
              + "Check client roles and credentials.",
          service,
          ex);

      return errorResponse(
          HttpStatus.FORBIDDEN,
          String.format("Access denied by %s service", service),
          "Client does not have required permissions. "
              + "Check Keycloak configuration (roles: manage-users, view-users, create-user) "
              + "and client credentials (client_id, client_secret).",
          ex,
          request);
    }

    if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
      log.error(
          "UNAUTHORIZED: Authentication failed with service {}. "
              + "Check credentials (client_secret, tokens).",
          service,
          ex);

      return errorResponse(
          HttpStatus.UNAUTHORIZED,
          String.format("Authentication failed with %s service", service),
          "Credentials are invalid or expired. "
              + "Check client_secret, realm, and Keycloak server URL.",
          ex,
          request);
    }

    log.error(
        "HTTP client error {} with service {}: {}",
        ex.getStatusCode(),
        service,
        ex.getResponseBodyAsString(),
        ex);

    return errorResponse(
        HttpStatus.BAD_REQUEST,
        String.format("Error calling %s service", service),
        String.format(
            "HTTP Code: %s - Response: %s", ex.getStatusCode(), ex.getResponseBodyAsString()),
        ex,
        request);
  }

  @ExceptionHandler({HttpServerErrorException.class})
  public ResponseEntity<Map<String, Object>> handleHttpServerError(
      HttpServerErrorException ex, HttpServletRequest request) {

    log.error(
        "SERVER ERROR: Service {} encountered an internal error ({}): {}",
        ex.getStatusCode(),
        ex.getResponseBodyAsString(),
        ex);

    return errorResponse(
        HttpStatus.SERVICE_UNAVAILABLE,
        String.format("Service %s is temporarily unavailable"),
        String.format("HTTP Code: %s - External service encountered an error.", ex.getStatusCode()),
        ex,
        request);
  }

  @ExceptionHandler({RestClientException.class})
  public ResponseEntity<Map<String, Object>> handleRestClientException(
      RestClientException ex, HttpServletRequest request) {
    log.error(
        "NETWORK ERROR: Unable to reach external service. "
            + "Check that the service is running and accessible.",
        ex);

    return errorResponse(
        HttpStatus.SERVICE_UNAVAILABLE,
        "External service unreachable",
        "Unable to connect to service. Check that the service is running "
            + "(docker ps) and the connection URL is correct.",
        ex,
        request);
  }

  @ExceptionHandler({ExternalServiceCallException.class})
  public ResponseEntity<Map<String, Object>> handleExternalServiceCallException(
      ExternalServiceCallException ex, HttpServletRequest request) {
    log.error("External service call failed", ex);
    return errorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, "Error calling external service", ex.getMessage(), ex, request);
  }

  @ExceptionHandler({ResourceNotFoundException.class})
  public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(
      ResourceNotFoundException ex, HttpServletRequest request) {
    log.error("Resource not found", ex);
    return errorResponse(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage(), ex, request);
  }

  @ExceptionHandler({InvalidEmailAddressException.class})
  public ResponseEntity<Map<String, Object>> handleInvalidEmailAddressException(
      InvalidEmailAddressException ex, HttpServletRequest request) {
    log.error("Invalid email address", ex);
    return errorResponse(HttpStatus.BAD_REQUEST, "Invalid email address", ex.getMessage(), ex, request);
  }

  @ExceptionHandler({InternalServerError.class})
  public ResponseEntity<Map<String, Object>> handleInternalServerError(
      InternalServerError ex, HttpServletRequest request) {
    log.error("Internal server error", ex);
    return errorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", ex.getMessage(), ex, request);
  }

  @ExceptionHandler({ConflictException.class})
  public ResponseEntity<Map<String, Object>> handleConflictException(
      ConflictException ex, HttpServletRequest request) {
    log.error("Conflict error", ex);
    return errorResponse(HttpStatus.CONFLICT, "Conflict detected", ex.getMessage(), ex, request);
  }

  @ExceptionHandler({IllegalArgumentException.class})
  public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
      IllegalArgumentException ex, HttpServletRequest request) {
    log.error("Invalid argument", ex);
    return errorResponse(HttpStatus.BAD_REQUEST, "Invalid argument", ex.getMessage(), ex, request);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    log.error("Validation error", ex);
    ResponseEntity<Map<String, Object>> response =
        errorResponse(HttpStatus.BAD_REQUEST, "Validation failed", "Request body validation error", ex, request);
    response.getBody().put("errors", fieldErrors(ex.getBindingResult().getFieldErrors()));
    return response;
  }

  @ExceptionHandler({BindException.class})
  public ResponseEntity<Map<String, Object>> handleBindException(
      BindException ex, HttpServletRequest request) {
    log.error("Bind error", ex);
    ResponseEntity<Map<String, Object>> response =
        errorResponse(HttpStatus.BAD_REQUEST, "Validation failed", "Request binding error", ex, request);
    response.getBody().put("errors", fieldErrors(ex.getFieldErrors()));
    return response;
  }

  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<Map<String, Object>> handleConstraintViolationException(
      ConstraintViolationException ex, HttpServletRequest request) {
    log.error("Constraint violation", ex);
    return errorResponse(
        HttpStatus.BAD_REQUEST, "Validation failed", ex.getMessage(), ex, request);
  }

  @ExceptionHandler({MissingServletRequestParameterException.class})
  public ResponseEntity<Map<String, Object>> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex, HttpServletRequest request) {
    log.error("Missing request parameter", ex);
    return errorResponse(
        HttpStatus.BAD_REQUEST, "Missing request parameter", ex.getMessage(), ex, request);
  }

  @ExceptionHandler({HttpMessageNotReadableException.class})
  public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex, HttpServletRequest request) {
    log.error("Malformed JSON request", ex);
    return errorResponse(
        HttpStatus.BAD_REQUEST, "Malformed JSON request", ex.getMostSpecificCause().getMessage(), ex, request);
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
    log.error("Method argument type mismatch", ex);
    String details =
        String.format(
            "Parameter '%s' must be of type %s",
            ex.getName(),
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
    return errorResponse(HttpStatus.BAD_REQUEST, "Type mismatch", details, ex, request);
  }

  @ExceptionHandler({NoHandlerFoundException.class})
  public ResponseEntity<Map<String, Object>> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpServletRequest request) {
    log.error("No handler found", ex);
    return errorResponse(HttpStatus.NOT_FOUND, "Not found", ex.getMessage(), ex, request);
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
    log.error("Method not allowed", ex);
    return errorResponse(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed", ex.getMessage(), ex, request);
  }

  @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
  public ResponseEntity<Map<String, Object>> handleHttpMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
    log.error("Unsupported media type", ex);
    return errorResponse(
        HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type", ex.getMessage(), ex, request);
  }

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<Map<String, Object>> handleAccessDeniedException(
      AccessDeniedException ex, HttpServletRequest request) {
    log.error("Access denied", ex);
    return errorResponse(HttpStatus.FORBIDDEN, "Access denied", ex.getMessage(), ex, request);
  }

  @ExceptionHandler({ResponseStatusException.class})
  public ResponseEntity<Map<String, Object>> handleResponseStatusException(
      ResponseStatusException ex, HttpServletRequest request) {
    log.error("Response status exception", ex);
    int statusCode = ex.getStatusCode().value();
    HttpStatus status =
        HttpStatus.resolve(statusCode) != null
            ? HttpStatus.resolve(statusCode)
            : HttpStatus.INTERNAL_SERVER_ERROR;
      assert status != null;
      return errorResponse(status, ex.getReason(), ex.getMessage(), ex, request);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<Map<String, Object>> defaultExceptionHandler(
      Exception ex, HttpServletRequest request) {
    log.error("Unhandled exception: {}", ex.getClass().getName(), ex);
    return errorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Unexpected error",
        ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred. Check logs for more details.",
        ex,
        request);
  }

  private String extractServiceName(HttpClientErrorException ex) {
    String responseBody = ex.getResponseBodyAsString();
    if (responseBody != null && responseBody.toLowerCase().contains("keycloak")) {
      return "Keycloak";
    }
    return "external";
  }

  private String safeMessage(Throwable throwable) {
    if (throwable == null) {
      return null;
    }
    String message = throwable.getMessage();
    return message != null ? message : throwable.getClass().getSimpleName();
  }

  private Throwable rootCause(Throwable throwable) {
    Throwable current = throwable;
    Throwable cause = current.getCause();
    while (cause != null && cause != current) {
      current = cause;
      cause = current.getCause();
    }
    return current;
  }

  private List<Map<String, String>> fieldErrors(List<FieldError> fieldErrors) {
    return fieldErrors.stream()
        .map(
            error -> {
              Map<String, String> item = new HashMap<>();
              item.put("field", error.getField());
              item.put("message", error.getDefaultMessage());
              item.put("rejectedValue", String.valueOf(error.getRejectedValue()));
              return item;
            })
        .toList();
  }
}
