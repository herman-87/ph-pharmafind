package com.ph.user.security;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BruteForceProtectionService {

  private static final int MAX_ATTEMPTS = 5;
  private static final Duration BLOCK_DURATION = Duration.ofMinutes(15);

  private final ConcurrentHashMap<String, AttemptRecord> attempts = new ConcurrentHashMap<>();

  public boolean isBlocked(String key) {
    AttemptRecord record = attempts.get(key);
    if (record == null) {
      return false;
    }
    if (record.attempts >= MAX_ATTEMPTS && Instant.now().isBefore(record.blockedUntil)) {
      return true;
    }
    if (Instant.now().isAfter(record.blockedUntil)) {
      attempts.remove(key);
      return false;
    }
    return false;
  }

  public boolean isBlocked(HttpServletRequest request) {
    return isBlocked(extractKey(request));
  }

  public void recordFailedAttempt(String key) {
    attempts.compute(
        key,
        (k, record) -> {
          if (record == null || Instant.now().isAfter(record.blockedUntil)) {
            return new AttemptRecord(1, Instant.now().plus(BLOCK_DURATION));
          }
          int newAttempts = record.attempts + 1;
          log.warn("Failed login attempt {} for key: {}", newAttempts, k);
          if (newAttempts >= MAX_ATTEMPTS) {
            log.warn("Account blocked due to brute force - key: {}", k);
          }
          return new AttemptRecord(newAttempts, Instant.now().plus(BLOCK_DURATION));
        });
  }

  public void recordFailedAttempt(HttpServletRequest request) {
    recordFailedAttempt(extractKey(request));
  }

  public void resetAttempts(String key) {
    attempts.remove(key);
  }

  public void resetAttempts(HttpServletRequest request) {
    resetAttempts(extractKey(request));
  }

  private String extractKey(HttpServletRequest request) {
    String ip = request.getRemoteAddr();
    String username = request.getParameter("username");
    if (username != null) {
      return ip + "|" + username.toLowerCase();
    }
    return ip;
  }

  private record AttemptRecord(int attempts, Instant blockedUntil) {}
}
