package com.ph.user.application.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.fastrelays.common.exception.ConflictException;
import com.ph.user.application.auth.dto.RegisterRequest;
import com.ph.user.domain.user.AuthProvider;
import com.ph.user.domain.user.Role;
import com.ph.user.domain.user.RoleName;
import com.ph.user.domain.user.User;
import com.ph.user.infrastructure.persistence.repository.RoleRepository;
import com.ph.user.infrastructure.persistence.repository.UserRepository;
import com.ph.user.jwt.JwtProperties;
import com.ph.user.jwt.JwtTokenService;
import com.ph.user.refresh.RefreshTokenService;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthApplicationServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private RoleRepository roleRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private AuthenticationManager authenticationManager;

  @Mock private JwtTokenService jwtTokenService;

  @Mock private RefreshTokenService refreshTokenService;

  @Mock private JwtProperties jwtProperties;

  @InjectMocks private AuthApplicationService authApplicationService;

  @Test
  void registerShouldCreateLocalUserWithNormalizedIdentity() {
    RegisterRequest request =
        new RegisterRequest("  John.Doe  ", "  John@Example.com  ", "strong-password");
    Role role = Role.builder().id(UUID.randomUUID()).name(RoleName.ROLE_USER).build();
    User persisted =
        User.builder()
            .id(UUID.randomUUID())
            .username("john.doe")
            .email("john@example.com")
            .password("encoded-password")
            .provider(AuthProvider.LOCAL)
            .roles(Set.of(role))
            .scopes(Set.of("openid", "profile", "email"))
            .enabled(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    when(userRepository.existsByUsername("john.doe")).thenReturn(false);
    when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
    when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(role));
    when(passwordEncoder.encode("strong-password")).thenReturn("encoded-password");
    when(userRepository.save(any(User.class))).thenReturn(persisted);

    User user = authApplicationService.register(request);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(captor.capture());
    User toSave = captor.getValue();

    assertThat(toSave.getUsername()).isEqualTo("john.doe");
    assertThat(toSave.getEmail()).isEqualTo("john@example.com");
    assertThat(toSave.getProvider()).isEqualTo(AuthProvider.LOCAL);
    assertThat(toSave.getRoles()).containsExactly(role);
    assertThat(toSave.getScopes()).containsExactlyInAnyOrder("openid", "profile", "email");
    assertThat(user.getUsername()).isEqualTo("john.doe");
    assertThat(user.getEmail()).isEqualTo("john@example.com");
    assertThat(user.getRoles()).extracting(r -> r.getName().name()).containsExactly("ROLE_USER");
  }

  @Test
  void registerShouldRejectExistingUsername() {
    RegisterRequest request =
        new RegisterRequest("existing-user", "user@example.com", "strong-password");
    when(userRepository.existsByUsername("existing-user")).thenReturn(true);

    assertThatThrownBy(() -> authApplicationService.register(request))
        .isInstanceOf(ConflictException.class)
        .hasMessage("username already exists");
  }
}
