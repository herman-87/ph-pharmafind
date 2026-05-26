package cm.fastrelays.common.security;

import java.util.UUID;

import cm.fastrelays.common.exception.BadRequestException;
import cm.fastrelays.common.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Slf4j
public class CurrentUser {
  private CurrentUser() {
    /* This utility class should not be instantiated */
  }


  public static UUID getUserId() {
    var jwt = getJwt();
    var sub = jwt.getSubject();
    if (sub == null || sub.isBlank()) {
      log.error("JWT subject (sub) claim not found");
      throw new ResourceNotFoundException("JWT subject (sub) claim not found");
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
      throw new BadRequestException("No authenticated user in security context");
    }
    if (auth instanceof JwtAuthenticationToken jwtAuth) {
      return jwtAuth.getToken();
    }
    log.error("Authentication is not a JwtAuthenticationToken");
    throw new BadRequestException("Authentication is not a JwtAuthenticationToken");
  }
}
