package com.ph.user.security.service;

import com.ph.user.domain.user.AuthProvider;
import com.ph.user.domain.user.RoleName;
import com.ph.user.domain.user.User;
import com.ph.user.infrastructure.persistence.repository.RoleRepository;
import com.ph.user.infrastructure.persistence.repository.UserRepository;
import java.util.Locale;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuth2UserService extends OidcUserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Override
  @Transactional
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    var googleUser = super.loadUser(userRequest);

    var providerId = googleUser.getSubject();
    var email =
        googleUser.getEmail() != null ? googleUser.getEmail().toLowerCase(Locale.ROOT) : null;
    var name =
        googleUser.getPreferredUsername() != null
            ? googleUser.getPreferredUsername()
            : googleUser.getGivenName();

    log.info("Google login attempt - providerId: {}, email: {}", providerId, email);

    var localUser =
        userRepository
            .findByProviderAndProviderId(AuthProvider.GOOGLE, providerId)
            .orElseGet(() -> createGoogleUser(providerId, email, name));

    var authorities = localUser.getAuthorities();

    log.info("Google user linked to local user: {}", localUser.getUsername());

    return new DefaultOidcUser(
        authorities, googleUser.getIdToken(), googleUser.getUserInfo(), "name");
  }

  private User createGoogleUser(String providerId, String email, String name) {
    var username = email != null ? email.split("@")[0] : "google-" + providerId;

    if (userRepository.existsByUsername(username)) {
      username = "google-" + providerId.substring(0, 8);
    }

    var defaultRole =
        roleRepository
            .findByName(RoleName.ROLE_USER)
            .orElseThrow(
                () -> new IllegalStateException("ROLE_USER role is missing from database"));

    var newUser =
        User.builder()
            .username(username)
            .email(email != null ? email : providerId + "@google.auth")
            .password(null)
            .provider(AuthProvider.GOOGLE)
            .providerId(providerId)
            .roles(Set.of(defaultRole))
            .scopes(Set.of("openid", "profile", "email"))
            .enabled(true)
            .emailVerified(true)
            .build();

    var saved = userRepository.save(newUser);
    log.info(
        "Created local user for Google account: {} (username: {})", email, saved.getUsername());
    return saved;
  }
}
