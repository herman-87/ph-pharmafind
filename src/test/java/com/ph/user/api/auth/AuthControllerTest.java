package com.ph.user.api.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cm.fastrelays.common.exception.ConflictException;
import com.ph.user.api.ApiExceptionHandler;
import com.ph.user.application.auth.AuthApplicationService;
import com.ph.user.application.auth.dto.RegisterRequest;
import com.ph.user.application.auth.mapper.AuthMapper;
import com.ph.user.domain.user.AuthProvider;
import com.ph.user.domain.user.Role;
import com.ph.user.domain.user.RoleName;
import com.ph.user.domain.user.User;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock private AuthApplicationService authApplicationService;

  @Mock private AuthMapper authMapper;

  @InjectMocks private AuthController authController;

  @Test
  void registerShouldReturnCreatedWithId() throws Exception {
    MockMvc mockMvc =
        MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new ApiExceptionHandler())
            .build();
    var userId = UUID.fromString("00000000-0000-0000-0000-000000000201");
    Role role = Role.builder().id(UUID.randomUUID()).name(RoleName.ROLE_USER).build();
    User user =
        User.builder()
            .id(userId)
            .username("john.doe")
            .email("john@example.com")
            .password("encoded")
            .provider(AuthProvider.LOCAL)
            .roles(Set.of(role))
            .scopes(Set.of("openid", "profile", "email"))
            .enabled(true)
            .build();

    when(authMapper.toRegisterRequest(any()))
        .thenReturn(new RegisterRequest("john.doe", "john@example.com", "strong-password"));
    when(authApplicationService.register(any(RegisterRequest.class))).thenReturn(user);

    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(APPLICATION_JSON)
                .content(
                    """
                                {
                                  "username": "john.doe",
                                  "email": "john@example.com",
                                  "password": "strong-password"
                                }
                                """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(userId.toString()));
  }

  @Test
  void registerShouldReturnConflictPayload() throws Exception {
    MockMvc mockMvc =
        MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new ApiExceptionHandler())
            .build();
    when(authMapper.toRegisterRequest(any()))
        .thenReturn(new RegisterRequest("john.doe", "john@example.com", "strong-password"));
    when(authApplicationService.register(any(RegisterRequest.class)))
        .thenThrow(new ConflictException("email already exists"));

    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(APPLICATION_JSON)
                .content(
                    """
                                {
                                  "username": "john.doe",
                                  "email": "john@example.com",
                                  "password": "strong-password"
                                }
                                """))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("email already exists"))
        .andExpect(jsonPath("$.path").value("/api/auth/register"));
  }

  @Test
  void registerShouldReturnValidationPayload() throws Exception {
    MockMvc mockMvc =
        MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new ApiExceptionHandler())
            .build();
    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(APPLICATION_JSON)
                .content(
                    """
                                {
                                  "username": "a",
                                  "email": "bad-email",
                                  "password": "short"
                                }
                                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("request validation failed"))
        .andExpect(jsonPath("$.violations.username").exists())
        .andExpect(jsonPath("$.violations.email").exists());
  }
}
