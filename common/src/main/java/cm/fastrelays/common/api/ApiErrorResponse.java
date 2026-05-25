package cm.fastrelays.common.api;

import java.time.OffsetDateTime;
import java.util.Map;

public record ApiErrorResponse(
    OffsetDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    Integer errorCode,
    Map<String, String> violations) {}
