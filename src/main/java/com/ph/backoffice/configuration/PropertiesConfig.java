package com.ph.backoffice.configuration;

import com.ph.backoffice.configuration.properties.AppSecurityProperties;
import com.ph.backoffice.jwt.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AppSecurityProperties.class, JwtProperties.class})
public class PropertiesConfig {}
