package com.ph.user.application.auth;

import cm.fastrelays.common.exception.ConflictException;
import cm.fastrelays.common.exception.ResourceNotFoundException;
import com.ph.user.application.auth.dto.LoginRequest;
import com.ph.user.application.auth.dto.LogoutRequest;
import com.ph.user.application.auth.dto.RefreshRequest;
import com.ph.user.application.auth.dto.RegisterRequest;
import com.ph.user.application.auth.dto.TokenResponse;
import com.ph.user.domain.user.AuthProvider;
import com.ph.user.domain.user.RoleName;
import com.ph.user.domain.user.User;
import com.ph.user.infrastructure.persistence.repository.RoleRepository;
import com.ph.user.infrastructure.persistence.repository.UserRepository;
import com.ph.user.jwt.JwtProperties;
import com.ph.user.jwt.JwtTokenService;
import com.ph.user.refresh.RefreshTokenService;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthApplicationService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;
  private final RefreshTokenService refreshTokenService;
  private final JwtProperties jwtProperties;

  @Transactional
  public User register(RegisterRequest request) {
    var normalizedUsername = normalizeUsername(request.username());
    var normalizedEmail = normalizeEmail(request.email());

    if (userRepository.existsByUsername(normalizedUsername)) {
      throw new ConflictException("username already exists");
    }
    if (userRepository.existsByEmail(normalizedEmail)) {
      throw new ConflictException("email already exists");
    }

    var defaultRole =
        roleRepository
            .findByName(RoleName.ROLE_USER)
            .orElseThrow(() -> new IllegalStateException("ROLE_USER is missing"));

    return userRepository.save(
        User.builder()
            .username(normalizedUsername)
            .email(normalizedEmail)
            .password(passwordEncoder.encode(request.password()))
            .provider(AuthProvider.LOCAL)
            .roles(Set.of(defaultRole))
            .scopes(Set.of("openid", "profile", "email"))
            .enabled(true)
            .build());
  }

  @Transactional
  public TokenResponse login(LoginRequest request) {
    var authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    var user = (User) authentication.getPrincipal();

    var accessToken = jwtTokenService.generateAccessToken(user);
    var refreshToken = refreshTokenService.createRefreshToken(user);

    return new TokenResponse(
        accessToken, refreshToken.getToken(), "Bearer", jwtProperties.accessTokenTtl().toSeconds());
  }

  @Transactional
  public TokenResponse refresh(RefreshRequest request) {
    var refreshToken = refreshTokenService.validateAndRotate(request.refreshToken());
    var accessToken = jwtTokenService.generateAccessToken(refreshToken.getUser());

    return new TokenResponse(
        accessToken, refreshToken.getToken(), "Bearer", jwtProperties.accessTokenTtl().toSeconds());
  }

  @Transactional
  public void logout(LogoutRequest request) {
    refreshTokenService.revokeByToken(request.refreshToken());
  }

  @Transactional(readOnly = true)
  public User getUserInfo(UUID userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
  }

  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  private String normalizeUsername(String username) {
    return username.trim().toLowerCase(Locale.ROOT);
  }

  private String normalizeEmail(String email) {
    return email.trim().toLowerCase(Locale.ROOT);
  }
}
