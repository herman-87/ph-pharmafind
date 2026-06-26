package com.ph.backoffice.configuration.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtDecoder jwtDecoder;
  private final CookieBearerTokenResolver cookieBearerTokenResolver;

  @Bean
  @Order(0)
  public SecurityFilterChain healthEndpointsSecurityFilterChain(HttpSecurity http)
      throws Exception {
    return http.securityMatcher("/actuator/health/**")
        .csrf(httpSecurityCsrfConfigurer -> {})
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .build();
  }

  @Bean
  @Order(1)
  public SecurityFilterChain publicEndpointsSecurityFilterChain(HttpSecurity http)
      throws Exception {
    return http.securityMatcher("/public/**")
        .csrf(httpSecurityCsrfConfigurer -> {})
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(httpSecurityCsrfConfigurer -> {})
        .authorizeHttpRequests(
            auth -> auth.requestMatchers("/me/**").authenticated().anyRequest().denyAll())
        .oauth2ResourceServer(
            oauth2 ->
                oauth2
                    .bearerTokenResolver(cookieBearerTokenResolver)
                    .jwt(
                        jwt ->
                            jwt.decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }

  private JwtAuthenticationConverter jwtAuthenticationConverter() {
    var converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(
        jwt -> {
          Collection<GrantedAuthority> authorities = new ArrayList<>();
          List<String> roles = jwt.getClaimAsStringList("roles");
          if (roles != null) {
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
          }
          List<String> scopes = jwt.getClaimAsStringList("scopes");
          if (scopes != null) {
            scopes.forEach(scope -> authorities.add(new SimpleGrantedAuthority("SCOPE_" + scope)));
          }
          return authorities;
        });
    return converter;
  }
}
