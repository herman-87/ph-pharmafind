package cm.fastrelays.common.api;

import cm.fastrelays.common.exception.ApiException;
import cm.fastrelays.common.exception.ExternalServiceCallException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

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
      body.put("errorCode", apiEx.getErrorCode());
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

}
