package com.ph.pharmafind.configuration.properties;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record AppSecurityProperties(
    String issuer, String frontendLoginUrl, List<String> allowedOrigins) {}
