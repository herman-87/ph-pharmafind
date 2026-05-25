package cm.fastrelays.common.security;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Slf4j
public class CurrentUser {

  public static UUID getUserId() {
    var jwt = getJwt();
    var sub = jwt.getSubject();
    if (sub == null || sub.isBlank()) {
      log.error("JWT subject (sub) claim not found");
      throw new RuntimeException("JWT subject (sub) claim not found");
    }
    return UUID.fromString(sub);
  }

  public static String getUserName() {
    return getJwt().getClaimAsString("username");
  }

  public static String getEmail() {
    return getJwt().getClaimAsString("email");
  }

  private static Jwt getJwt() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      log.error("No Authentication found in SecurityContext");
      throw new RuntimeException("No authenticated user in security context");
    }
    if (auth instanceof JwtAuthenticationToken jwtAuth) {
      return jwtAuth.getToken();
    }
    log.error("Authentication is not a JwtAuthenticationToken");
    throw new RuntimeException("Authentication is not a JwtAuthenticationToken");
  }
}
