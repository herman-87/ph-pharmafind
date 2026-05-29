package com.ph.backoffice.configuration.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({SecurityProperties.class, JwtProperties.class})
public class GlobalPropertiesConfig {}
