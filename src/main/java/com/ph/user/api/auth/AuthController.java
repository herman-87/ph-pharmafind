package com.ph.user.api.auth;

import com.ph.user.application.auth.AuthApplicationService;
import com.ph.user.application.auth.mapper.AuthMapper;
import com.ph.user.generated.api.AuthenticationApi;
import com.ph.user.generated.model.LoginRequestDTO;
import com.ph.user.generated.model.LogoutRequestDTO;
import com.ph.user.generated.model.RefreshRequestDTO;
import com.ph.user.generated.model.RegisterRequestDTO;
import com.ph.user.generated.model.RegisterResponseDTO;
import com.ph.user.generated.model.TokenResponseDTO;
import com.ph.user.generated.model.UserInfoResponseDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthenticationApi {

  private final AuthApplicationService authApplicationService;
  private final AuthMapper authMapper;

  public AuthController(AuthApplicationService authApplicationService, AuthMapper authMapper) {
    this.authApplicationService = authApplicationService;
    this.authMapper = authMapper;
  }

  @GetMapping("/hello")
  public ResponseEntity<String> hello() {
    return ResponseEntity.ok("hello world");
  }

  @Override
  public ResponseEntity<RegisterResponseDTO> register(
      @Valid RegisterRequestDTO registerRequestDTO) {
    var request = authMapper.toRegisterRequest(registerRequestDTO);
    var user = authApplicationService.register(request);
    var response = new RegisterResponseDTO().id(user.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<TokenResponseDTO> login(@Valid LoginRequestDTO loginRequestDTO) {
    var request = authMapper.toLoginRequest(loginRequestDTO);
    var tokenResponse = authApplicationService.login(request);
    return ResponseEntity.ok(authMapper.toTokenResponseDTO(tokenResponse));
  }

  @Override
  public ResponseEntity<TokenResponseDTO> refreshToken(@Valid RefreshRequestDTO refreshRequestDTO) {
    var request = authMapper.toRefreshRequest(refreshRequestDTO);
    var tokenResponse = authApplicationService.refresh(request);
    return ResponseEntity.ok(authMapper.toTokenResponseDTO(tokenResponse));
  }

  @Override
  public ResponseEntity<Void> logout(@Valid LogoutRequestDTO logoutRequestDTO) {
    var request = authMapper.toLogoutRequest(logoutRequestDTO);
    authApplicationService.logout(request);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<UserInfoResponseDTO> getCurrentUserInfo() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    var jwt = (Jwt) auth.getPrincipal();
    var userId = UUID.fromString(jwt.getSubject());
    var user = authApplicationService.getUserInfo(userId);
    return ResponseEntity.ok(authMapper.toUserInfoResponseDTO(user));
  }

  @Override
  public ResponseEntity<List<UserInfoResponseDTO>> getAllUsers() {
    var users = authApplicationService.getAllUsers();
    return ResponseEntity.ok(authMapper.toUserInfoResponseDTO(users));
  }
}
