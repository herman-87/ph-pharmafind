package com.ph.user.oauth;

import com.ph.user.configuration.properties.AppSecurityProperties;
import com.ph.user.domain.user.AuthProvider;
import com.ph.user.infrastructure.persistence.repository.UserRepository;
import com.ph.user.jwt.JwtTokenService;
import com.ph.user.refresh.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtTokenService jwtTokenService;
  private final RefreshTokenService refreshTokenService;
  private final UserRepository userRepository;
  private final AppSecurityProperties appSecurityProperties;

  public OAuth2AuthenticationSuccessHandler(
      JwtTokenService jwtTokenService,
      RefreshTokenService refreshTokenService,
      UserRepository userRepository,
      AppSecurityProperties appSecurityProperties) {
    this.jwtTokenService = jwtTokenService;
    this.refreshTokenService = refreshTokenService;
    this.userRepository = userRepository;
    this.appSecurityProperties = appSecurityProperties;
  }

  @Override
  @Transactional
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    var principal = (OidcUser) authentication.getPrincipal();
    var providerId = principal.getSubject();

    var user =
        userRepository
            .findByProviderAndProviderId(AuthProvider.GOOGLE, providerId)
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "User not found after Google login, providerId: " + providerId));

    var accessToken = jwtTokenService.generateAccessToken(user);
    var refreshToken = refreshTokenService.createRefreshToken(user);

    var targetUrl =
        UriComponentsBuilder.fromUriString(appSecurityProperties.frontendLoginUrl())
            .queryParam("access_token", accessToken)
            .queryParam("refresh_token", refreshToken.getToken())
            .queryParam("token_type", "Bearer")
            .build()
            .toUriString();

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}
