package com.ph.user.jwt;

import java.nio.file.Path;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
    String issuer, Duration accessTokenTtl, Duration refreshTokenTtl, RsaKeys rsa) {
  public record RsaKeys(Path privateKeyPath, Path publicKeyPath) {}
}
