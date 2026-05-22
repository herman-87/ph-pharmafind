package com.ph.user.security;

import com.ph.user.configuration.properties.AppSecurityProperties;
import com.ph.user.oauth.OAuth2AuthenticationSuccessHandler;
import com.ph.user.security.service.GoogleOAuth2UserService;
import com.ph.user.security.service.JpaUserDetailsService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JpaUserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;
  private final AppSecurityProperties appSecurityProperties;
  private final GoogleOAuth2UserService googleOAuth2UserService;
  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
  private final JwtDecoder jwtDecoder;

  public SecurityConfig(
      JpaUserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder,
      AppSecurityProperties appSecurityProperties,
      GoogleOAuth2UserService googleOAuth2UserService,
      OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
      JwtDecoder jwtDecoder) {
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
    this.appSecurityProperties = appSecurityProperties;
    this.googleOAuth2UserService = googleOAuth2UserService;
    this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
    this.jwtDecoder = jwtDecoder;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        HttpMethod.POST,
                        "/api/auth/register",
                        "/api/auth/login",
                        "/api/auth/refresh",
                        "/api/auth/logout")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/hello", "/users")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/user/info")
                    .authenticated()
                    .requestMatchers("/oauth2/**", "/login/**", "/actuator/health")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .oauth2Login(
            oauth2 ->
                oauth2
                    .userInfoEndpoint(userInfo -> userInfo.oidcUserService(googleOAuth2UserService))
                    .successHandler(oAuth2AuthenticationSuccessHandler))
        .oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(
                    jwt ->
                        jwt.decoder(jwtDecoder)
                            .jwtAuthenticationConverter(jwtAuthenticationConverter())))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(daoAuthenticationProvider());

    return http.build();
  }

  @Bean
  DaoAuthenticationProvider daoAuthenticationProvider() {
    var provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  @Bean
  AuthenticationManager authenticationManager() {
    return new ProviderManager(daoAuthenticationProvider());
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    var configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(appSecurityProperties.allowedOrigins());
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
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
