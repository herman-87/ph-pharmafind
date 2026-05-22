package cm.fastrelays.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Slf4j
public class CurrentUser {

  /**
   * Returns preferred_username claim for the currently authenticated user. Falls back to
   * Authentication.getName() if claim not present.
   */
  public static String getUserName() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      log.error("No Authentication found in SecurityContext");
      throw new RuntimeException("No authenticated user in security context");
    }

    if (auth instanceof JwtAuthenticationToken jwtAuth) {
      Jwt jwt = jwtAuth.getToken();
      String preferred = jwt.getClaimAsString("preferred_username");
      if (preferred != null && !preferred.isBlank()) {
        return preferred;
      }
    }

    String fallback = auth.getName();
    if (fallback == null || fallback.isBlank()) {
      log.error("The current username is null or blank");
      throw new RuntimeException("The current username is null or blank");
    }
    return fallback;
  }

  /** Returns the subject (sub) claim for the currently authenticated user. */
  public static String getUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      log.error("No Authentication found in SecurityContext");
      throw new RuntimeException("No authenticated user in security context");
    }
    if (auth instanceof JwtAuthenticationToken jwtAuth) {
      Jwt jwt = jwtAuth.getToken();
      String sub = jwt.getClaimAsString("sub");
      if (sub != null && !sub.isBlank()) {
        return sub;
      }
    }
    log.error("JWT subject (sub) claim not found for current user");
    throw new RuntimeException("JWT subject (sub) claim not found for current user");
  }
}
