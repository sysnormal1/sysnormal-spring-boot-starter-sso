package com.sysnormal.security.auth.sso.starter.configs;

import com.sysnormal.security.auth.sso.starter.properties.auth.google.GoogleAuthProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GoogleAuthProperties.class)
@ConditionalOnProperty(prefix = "spring.auth.google", name = "enabled", havingValue = "true")
public class GoogleAuthAutoConfiguration {

}
