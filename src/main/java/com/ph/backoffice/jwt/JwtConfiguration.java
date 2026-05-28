package com.ph.backoffice.jwt;

import cm.fastrelays.common.exception.InternalServerError;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfiguration {

  private static final Logger log = LoggerFactory.getLogger(JwtConfiguration.class);

  private final JwtProperties jwtProperties;

  public JwtConfiguration(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
  }

  @Bean
  JwtDecoder jwtDecoder() {
    try {
      log.info("Fetching JWKS from {}", jwtProperties.jwkSetUri());
      var jwkSet = JWKSet.load(URI.create(jwtProperties.jwkSetUri()).toURL());
      var rsaJWK =
          jwkSet.getKeys().stream()
              .filter(RSAKey.class::isInstance)
              .map(RSAKey.class::cast)
              .findFirst()
              .orElseThrow(() -> new RuntimeException("No RSA key found in JWKS"));
      var publicKey = rsaJWK.toPublicKey();
      log.info("JWKS loaded successfully → creating JwtDecoder");
      return NimbusJwtDecoder.withPublicKey((RSAPublicKey) publicKey).build();
    } catch (Exception e) {
      throw new InternalServerError(
          "Failed to load JWKS at startup from " + jwtProperties.jwkSetUri() + " " + e);
    }
  }
}
