package com.ph.user.jwt;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfiguration {

  private static final Logger log = LoggerFactory.getLogger(JwtConfiguration.class);

  private final JwtProperties jwtProperties;

  public JwtConfiguration(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
  }

  @Bean
  RsaKeyPair rsaKeyPair() throws IOException, GeneralSecurityException {
    var privateKeyPath = resolvePath(jwtProperties.rsa().privateKeyPath());
    var publicKeyPath = resolvePath(jwtProperties.rsa().publicKeyPath());

    if (Files.notExists(privateKeyPath) || Files.notExists(publicKeyPath)) {
      generateAndSaveKeys(privateKeyPath, publicKeyPath);
    }

    var privateKey = readPrivateKey(privateKeyPath);
    var publicKey = readPublicKey(publicKeyPath);
    return new RsaKeyPair(privateKey, publicKey);
  }

  @Bean
  JwtEncoder jwtEncoder(RsaKeyPair rsaKeyPair) {
    var jwk =
        new RSAKey.Builder(rsaKeyPair.publicKey())
            .privateKey(rsaKeyPair.privateKey())
            .keyID(UUID.randomUUID().toString())
            .build();
    JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwkSource);
  }

  @Bean
  JwtDecoder jwtDecoder(RsaKeyPair rsaKeyPair) {
    return NimbusJwtDecoder.withPublicKey(rsaKeyPair.publicKey()).build();
  }

  private Path resolvePath(Path path) {
    if (path.isAbsolute()) {
      return path;
    }
    return Path.of(System.getProperty("java.io.tmpdir"), "jwt-keys").resolve(path);
  }

  private void generateAndSaveKeys(Path privateKeyPath, Path publicKeyPath)
      throws IOException, GeneralSecurityException {
    var generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);
    var keyPair = generator.generateKeyPair();

    Files.createDirectories(privateKeyPath.getParent());

    var privatePem =
        """
            -----BEGIN PRIVATE KEY-----
            %s
            -----END PRIVATE KEY-----
            """
            .formatted(Base64.getMimeEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
    Files.writeString(privateKeyPath, privatePem);

    var publicPem =
        """
            -----BEGIN PUBLIC KEY-----
            %s
            -----END PUBLIC KEY-----
            """
            .formatted(Base64.getMimeEncoder().encodeToString(keyPair.getPublic().getEncoded()));
    Files.writeString(publicKeyPath, publicPem);

    log.info("RSA key pair generated and saved to {} and {}", privateKeyPath, publicKeyPath);
  }

  private RSAPrivateKey readPrivateKey(Path path) throws IOException, GeneralSecurityException {
    var key =
        Files.readString(path)
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");
    var encoded = Base64.getDecoder().decode(key);
    var keyFactory = KeyFactory.getInstance("RSA");
    return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encoded));
  }

  private RSAPublicKey readPublicKey(Path path) throws IOException, GeneralSecurityException {
    var key =
        Files.readString(path)
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");
    var encoded = Base64.getDecoder().decode(key);
    var keyFactory = KeyFactory.getInstance("RSA");
    return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(encoded));
  }

  public record RsaKeyPair(RSAPrivateKey privateKey, RSAPublicKey publicKey) {}
}
