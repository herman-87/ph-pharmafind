package com.ph.user.refresh;

import com.ph.user.domain.user.User;
import com.ph.user.jwt.JwtProperties;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtProperties jwtProperties;

  public RefreshTokenService(
      RefreshTokenRepository refreshTokenRepository, JwtProperties jwtProperties) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.jwtProperties = jwtProperties;
  }

  @Transactional
  public RefreshToken createRefreshToken(User user) {
    var entity =
        RefreshToken.builder()
            .token(UUID.randomUUID().toString())
            .user(user)
            .expiresAt(Instant.now().plus(jwtProperties.refreshTokenTtl()))
            .revoked(false)
            .createdAt(Instant.now())
            .build();
    return refreshTokenRepository.save(entity);
  }

  @Transactional
  public RefreshToken validateAndRotate(String tokenValue) {
    var refreshToken =
        refreshTokenRepository
            .findByToken(tokenValue)
            .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

    if (refreshToken.isRevoked()) {
      throw new IllegalArgumentException("Refresh token has been revoked");
    }

    if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
      throw new IllegalArgumentException("Refresh token has expired");
    }

    refreshToken.setRevoked(true);
    refreshTokenRepository.save(refreshToken);

    return createRefreshToken(refreshToken.getUser());
  }

  @Transactional
  public void revokeByToken(String tokenValue) {
    refreshTokenRepository
        .findByToken(tokenValue)
        .ifPresent(
            token -> {
              token.setRevoked(true);
              refreshTokenRepository.save(token);
            });
  }
}
