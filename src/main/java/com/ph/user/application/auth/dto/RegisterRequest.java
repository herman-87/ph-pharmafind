package com.ph.user.application.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank
        @Size(min = 4, max = 120)
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "username contains unsupported characters")
        String username,
    @NotBlank @Email @Size(max = 190) String email,
    String password) {}
