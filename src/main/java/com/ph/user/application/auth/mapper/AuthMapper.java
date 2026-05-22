package com.ph.user.application.auth.mapper;

import com.ph.user.application.auth.dto.LoginRequest;
import com.ph.user.application.auth.dto.LogoutRequest;
import com.ph.user.application.auth.dto.RefreshRequest;
import com.ph.user.application.auth.dto.RegisterRequest;
import com.ph.user.application.auth.dto.TokenResponse;
import com.ph.user.domain.user.User;
import com.ph.user.generated.model.LoginRequestDTO;
import com.ph.user.generated.model.LogoutRequestDTO;
import com.ph.user.generated.model.RefreshRequestDTO;
import com.ph.user.generated.model.RegisterRequestDTO;
import com.ph.user.generated.model.TokenResponseDTO;
import com.ph.user.generated.model.UserInfoResponseDTO;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

  // Generated DTO -> Internal DTO
  RegisterRequest toRegisterRequest(RegisterRequestDTO dto);

  LoginRequest toLoginRequest(LoginRequestDTO dto);

  RefreshRequest toRefreshRequest(RefreshRequestDTO dto);

  LogoutRequest toLogoutRequest(LogoutRequestDTO dto);

  // Internal DTO -> Generated DTO
  TokenResponseDTO toTokenResponseDTO(TokenResponse internal);

  // Entity -> Generated DTO
  @Mapping(
      target = "roles",
      expression = "java(user.getRoles().stream().map(r -> r.getName().name()).toList())")
  @Mapping(target = "provider", expression = "java(user.getProvider().name())")
  UserInfoResponseDTO toUserInfoResponseDTO(User user);

  @IterableMapping(elementTargetType = UserInfoResponseDTO.class)
  List<UserInfoResponseDTO> toUserInfoResponseDTO(List<User> users);
}
