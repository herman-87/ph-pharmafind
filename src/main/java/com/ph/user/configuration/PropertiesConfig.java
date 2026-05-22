package com.ph.user.configuration;

import com.ph.user.configuration.properties.AppSecurityProperties;
import com.ph.user.jwt.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AppSecurityProperties.class, JwtProperties.class})
public class PropertiesConfig {}
