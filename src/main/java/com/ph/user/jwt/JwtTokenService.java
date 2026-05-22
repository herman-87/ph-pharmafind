package com.ph.user.jwt;

import com.ph.user.domain.user.User;
import java.time.Instant;
import java.util.List;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

  private final JwtEncoder jwtEncoder;
  private final JwtProperties jwtProperties;

  public JwtTokenService(JwtEncoder jwtEncoder, JwtProperties jwtProperties) {
    this.jwtEncoder = jwtEncoder;
    this.jwtProperties = jwtProperties;
  }

  public String generateAccessToken(User user) {
    var now = Instant.now();
    var claims =
        JwtClaimsSet.builder()
            .issuer(jwtProperties.issuer())
            .subject(user.getId().toString())
            .issuedAt(now)
            .expiresAt(now.plus(jwtProperties.accessTokenTtl()))
            .claim("username", user.getUsername())
            .claim("email", user.getEmail())
            .claim("roles", user.getRoles().stream().map(role -> role.getName().name()).toList())
            .claim("scopes", List.copyOf(user.getScopes()))
            .build();

    return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }
}
