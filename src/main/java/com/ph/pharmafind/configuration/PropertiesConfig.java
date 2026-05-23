package com.ph.pharmafind.configuration;

import com.ph.pharmafind.configuration.properties.AppSecurityProperties;
import com.ph.pharmafind.jwt.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AppSecurityProperties.class, JwtProperties.class})
public class PropertiesConfig {}
