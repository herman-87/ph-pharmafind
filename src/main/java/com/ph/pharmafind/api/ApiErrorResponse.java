package com.ph.pharmafind.api;

import java.time.OffsetDateTime;
import java.util.Map;

public record ApiErrorResponse(
    OffsetDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    String errorCode,
    Map<String, String> violations) {}
